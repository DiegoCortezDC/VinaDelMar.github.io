package org.utl.dsm503.utl;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.IOException;

public class MailSender {

    public static void sendEmail(String to, String subject, String text, String attachmentPath) throws MessagingException, IOException {
        String from = "cortezgonzalezjuandiego3@gmail.com"; // Tu correo
        final String username = "cortezgonzalezjuandiego3@gmail.com"; // Tu correo
        final String password = "zcte mfci kxxq swef "; // Tu contraseña de aplicación si tienes activada la verificación en dos pasos

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");  // Activar logging detallado para diagnóstico

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(text);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(attachmentPath);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);

        Transport.send(message);
    }
}
