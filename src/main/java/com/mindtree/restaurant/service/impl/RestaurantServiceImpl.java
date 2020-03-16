package com.mindtree.restaurant.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtree.restaurant.client.RestaurantPaymentClient;
import com.mindtree.restaurant.exception.AuthenticationFailureException;
import com.mindtree.restaurant.exception.InvalidRequestException;
import com.mindtree.restaurant.exception.TransferFailureException;
import com.mindtree.restaurant.model.ConfirmBooking;
import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.PaymentDTO;
import com.mindtree.restaurant.model.Restaurant;
import com.mindtree.restaurant.model.Restaurants;
import com.mindtree.restaurant.model.User;
import com.mindtree.restaurant.service.RestaurantService;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	@Autowired
	private RestaurantPaymentClient paymentClient;
	
	public RestaurantPaymentClient getPaymentClient() {
		return paymentClient;
	}

	public void setPaymentClient(RestaurantPaymentClient paymentClient) {
		this.paymentClient = paymentClient;
	}


    @Value("${app.smtp.username}")
    private String username;
    @Value("${app.smtp.password}")
    private String password;
    @Value("${app.smtp.port}")
    private String port;
    @Value("${app.smtp.host}")
    private String host;
    private static final Map<String, User> USER_MAP = new HashMap<String, User>();
    private static Restaurants RESTAURANTS = new Restaurants();
    {
        ObjectMapper mapper = new ObjectMapper();
        try (Reader reader = new InputStreamReader(
            getClass().getClassLoader().getResourceAsStream("Restaurent_menu.txt"))) {
            RESTAURANTS = mapper.readValue(reader, Restaurants.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
            getClass().getClassLoader().getResourceAsStream("Restaurent_merchant_details.csv")))) {
            br.readLine();
            String line = br.readLine();
            while (!StringUtils.isEmpty(line)) {
                String[] restaurant = line.split(",");
                User user = new User();
                user.setRestaurantName(restaurant[0]);
                user.setUsername(restaurant[1]);
                user.setPassword(restaurant[2]);
                user.setEmail(restaurant[3]);
                USER_MAP.put(restaurant[1].toLowerCase(), user);
                line = br.readLine();
            }
        }

        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
	public ConfirmBooking placeOrder(Order order)
			throws AuthenticationFailureException, TransferFailureException, InvalidRequestException {
		order.setRestaurantName(USER_MAP.get(order.getRestaurantUsername().toLowerCase()).getRestaurantName());
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("EEE, d MMM yyyy");
		order.setDate(sf.format(date));
		order.setPaymentMode("Minto Pay");
		order.setTotal(order.getOrderItems().stream().map(it -> it.getQty() * it.getMenuItem().getPrice()).reduce(0,
				Integer::sum));
		order.setTxnId("Transaction Id");
		Thread th = new Thread(() -> sendMail(order));
		th.start();
		PaymentDTO paymentDTO = constructPaymentDTO(order);
		return paymentClient.makePayment(paymentDTO);
	}

    private PaymentDTO constructPaymentDTO(Order order) {
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setAmount(order.getTotal());
		paymentDTO.setEmail(order.getEmail());
		paymentDTO.setFaceId(order.getFaceId());
		paymentDTO.setPartner("RESTAURANT");
		return paymentDTO;
	}

	@Override
    public User login(User user) throws AuthenticationFailureException  {
        User userRes = USER_MAP.get(user.getUsername().toLowerCase());
        if (userRes != null && userRes.getPassword().equals(user.getPassword())) {
            for (Restaurant restaurant : RESTAURANTS.getRestaurents()) {
                if (restaurant.getName().equalsIgnoreCase(userRes.getRestaurantName()))
                    userRes.setMenuGruop(restaurant.getMenuGruop());
            }
            return userRes;
        }
        throw new AuthenticationFailureException("Either username or password is not correct!!!");
    }

    private void sendMail(Order order) {

        byte[] invoicePdf = createInvoice(order);
        String to = order.getEmail();

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Get the Session object.
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject("Invoice From " + order.getRestaurantName());

            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText("Please Find the attached Invoice");

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(invoicePdf, "application/pdf");
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("Invoice.pdf");
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createInvoice(Order order) {
        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("order", order);
            JasperReport report = (JasperReport) JRLoader
                .loadObject(this.getClass().getResourceAsStream("/invoice.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            byte[] pdfReport = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdfReport;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
