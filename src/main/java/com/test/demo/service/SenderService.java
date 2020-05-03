package com.test.demo.service;


import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class SenderService {

    private static Logger log = Logger.getLogger(SenderService.class.getName());

    static void sendMail(String filename, String email, String numeClient) {

        log.info("Set up organization mail security...");

        final String username = "javatest12345678900@gmail.com";
        final String password = "Parola.1234";

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            log.info("Building new message");
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(username));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));

            // Set Subject: header field
            message.setSubject("Client " + numeClient);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("This is the summary of your operation.");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            filename = filename.replace("\\", "/");
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            String[] arrOfStr = filename.split("/");
            System.out.println(arrOfStr[arrOfStr.length - 1]);
            messageBodyPart.setFileName(arrOfStr[arrOfStr.length - 1]);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            log.info("Mail sent...");
        } catch (MessagingException e) {
            log.info("Something went wrong while executing Transport.send(...) method...");
            e.printStackTrace();
        }
    }
}
