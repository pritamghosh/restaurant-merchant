package com.mindtree.restaurant.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.OrderItem;
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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private static final Map<String, User> userMap = new HashMap<String, User>();
    private static Restaurants restaurents = new Restaurants();
    {
        ObjectMapper mapper = new ObjectMapper();
        try {
            restaurents = mapper.readValue(
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
                userMap.put(restaurant[1].toLowerCase(), user);
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
    public void addOrder(OrderItem order) throws Exception {
    }

    @Override
    public User login(User user) throws Exception {
        User userRes = userMap.get(user.getUsername().toLowerCase());
        if (userRes != null && userRes.getPassword().equals(user.getPassword())) {
            for (Restaurant restaurant : restaurents.getRestaurents()) {
                if (restaurant.getName().equalsIgnoreCase(userRes.getRestaurantName()))
                    userRes.setMenuGruop(restaurant.getMenuGruop());
            }
            return userRes;
        }
        throw new Exception("Bad Credential");
    }
    
    
    private byte[] createInvoice(Order order) throws FileNotFoundException {
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
