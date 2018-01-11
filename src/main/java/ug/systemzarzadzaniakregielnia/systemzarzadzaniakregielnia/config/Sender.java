package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.config;



import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Sender {
    public static void sendEmail(String to, String title, String content) throws MessagingException {
        String smtpServer = "smtp.gmail.com";
        int port = 587;
        String userid = "bowlingmanagerug";
        String password = "bowlingmanager12345";
        String contentType = "text/html";
        String from = "bowlingmanagerug@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", "587");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.from", to);

        Session mailSession = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userid, password);
                    }
                });


        MimeMessage message = new MimeMessage(mailSession);
        message.addFrom(InternetAddress.parse(from));
        message.setRecipients(Message.RecipientType.TO, to);
        message.setSubject(title);
        message.setContent(content, contentType);

        Transport transport = mailSession.getTransport();
        try {
            transport.connect(smtpServer, port, userid, password);
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
        } catch (Exception e) {
            e.printStackTrace();

        }
        transport.close();
    }
}