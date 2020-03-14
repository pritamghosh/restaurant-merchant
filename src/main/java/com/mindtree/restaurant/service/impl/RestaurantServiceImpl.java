package com.mindtree.restaurant.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.Restaurant;
import com.mindtree.restaurant.model.Restaurants;
import com.mindtree.restaurant.model.User;
import com.mindtree.restaurant.service.RestaurantService;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;


@Service
public class RestaurantServiceImpl implements RestaurantService {
    
    @Value("${app.smtp.username}")
    private String username;
    @Value("${app.smtp.password}")
    private String password;
    private static final Map<String, User> USER_MAP = new HashMap<String, User>();
    private static Restaurants RESTAURANTS = new Restaurants();
    {
        ObjectMapper mapper = new ObjectMapper();
        try {
            RESTAURANTS = mapper.readValue(
                new File(getClass().getClassLoader().getResource("Restaurent_menu.txt").getFile()), Restaurants.class);
        }
        catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    {
        try (BufferedReader br = new BufferedReader(
            new FileReader(getClass().getClassLoader().getResource("Restaurent_merchant_details.csv").getFile()))) {
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
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void placeOrder(Order order) throws Exception {
        order.setRestaurantName(USER_MAP.get(order.getRestaurantUsername().toLowerCase()).getRestaurantName());
        order.setDate(new Date());
        //call payment
        order.setTxnId("txnid");
        sendMail(order);
    }

    @Override
    public User login(User user) throws Exception {
        User userRes = USER_MAP.get(user.getUsername().toLowerCase());
        if(userRes == null ) {
            throw new Exception("Empty "+USER_MAP.size());
        }
        if (userRes != null && userRes.getPassword().equals(user.getPassword())) {
            for (Restaurant restaurant : RESTAURANTS.getRestaurents()) {
                if (restaurant.getName().equalsIgnoreCase(userRes.getRestaurantName()))
                    userRes.setMenuGruop(restaurant.getMenuGruop());
            }
            return userRes;
        }
        throw new Exception("Bad Credential");
    }
    
    private void sendMail(Order order){
        
        byte[] invoicePdf = createInvoice(order);
        String to = order.getEmail();

        // Sender's email ID needs to be mentioned
        String from = USER_MAP.get(order.getRestaurantUsername().toLowerCase()).getEmail();// "fromemail@gmail.com";


        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");
        
        
        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
              }
           });

        try {
           // Create a default MimeMessage object.
           Message message = new MimeMessage(session);

           // Set From: header field of the header.
           message.setFrom(new InternetAddress(from));

           // Set To: header field of the header.
           message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse(to));

           // Set Subject: header field
           message.setSubject("Invoice From "+order.getRestaurantName());

           // Create the message part
           BodyPart messageBodyPart = new MimeBodyPart();

           // Now set the actual message
           messageBodyPart.setText("This is message body");

           // Create a multipar message
           Multipart multipart = new MimeMultipart();

           // Set text message part
           multipart.addBodyPart(messageBodyPart);

           // Part two is attachment
           messageBodyPart = new MimeBodyPart();
           DataSource source  = new ByteArrayDataSource(invoicePdf, "pdf"); 
           messageBodyPart.setDataHandler(new DataHandler(source));
           messageBodyPart.setFileName("Invoice.pdf");
           multipart.addBodyPart(messageBodyPart);

           // Send the complete message parts
           message.setContent(multipart);

           // Send message
           Transport.send(message);

    
        } catch (MessagingException e) {
           throw new RuntimeException(e);
        }
    }
    
    private byte[] createInvoice(Order order)  {
        try {

          Map<String, Object> parameters = new HashMap<>();
//          JRBeanCollectionDataSource details = new JRBeanCollectionDataSource(getInvoice().getDetails());
//          parameters.put("details", details);
          parameters.put("order", order);
          
          JasperReport report = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/invoice.jasper"));
          JasperPrint jasperPrint
          = JasperFillManager.fillReport(report, parameters,new JREmptyDataSource());
          byte[] pdfReport=
          JasperExportManager.exportReportToPdf(jasperPrint);
          

          return pdfReport;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void exportToPdf(String fileName, JasperPrint jasperPrint) throws JRException {

        // print report to file
        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));

        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("Test Author");
        exportConfig.setEncrypted(false);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);
        exporter.exportReport();
    }

}
