package com.fundamental.utils;

import com.fundamental.model.Parametro;
import com.fundamental.services.SvcGeneral;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Henry Barrientos
 */
public class Mail implements Runnable {

    String from, to, subject, content;
    List<String> pathsAttachments;
    Properties props = new Properties();
    Session session;

    public Mail(String to, String subject, String content, List<String> pathsAttachments) {
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.pathsAttachments = pathsAttachments;
        SvcGeneral service = new SvcGeneral();
        Parametro parametro = service.getParameterByName("CORREO_NOTIFICACION_IP_PORT");
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.host", parametro.getValor().split(Constant.SEPARATOR_PARAM)[0]);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.host", parametro.getValor().split(Constant.SEPARATOR_PARAM)[0]);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", parametro.getValor().split(Constant.SEPARATOR_PARAM)[1]);
        //props.put("mail.debug", "true");  
        props.put("mail.smtp.socketFactory.port", parametro.getValor().split(Constant.SEPARATOR_PARAM)[1]);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        final Parametro parUserPass = service.getParameterByName("CORREO_NOTIFICACION_AUTH");
        from = parUserPass.getValor().split(Constant.SEPARATOR_PARAM)[0];
        service.closeConnections();
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(parUserPass.getValor().split(Constant.SEPARATOR_PARAM)[0], parUserPass.getValor().split(Constant.SEPARATOR_PARAM)[1]);
            }
        });
    }

    @Override
    public void run() {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            to = (to.contains(";")) ? to.replace(";", ",") : to;
            if (to.contains(",")) {
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            } else {
                msg.setRecipients(Message.RecipientType.TO, to);
            }
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            if (pathsAttachments != null) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(content, "text/html");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                // Part two is attachment
                DataSource source;
                for (String pathToAttachment : pathsAttachments) {
                    messageBodyPart = new MimeBodyPart();
                    source = new FileDataSource(pathToAttachment);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(pathToAttachment.substring(pathToAttachment.lastIndexOf(File.separator) + 1, pathToAttachment.length()));
                    multipart.addBodyPart(messageBodyPart);
                    msg.setContent(multipart);
                }
            } else {
                msg.setContent(content, "text/html");
            }
            Transport.send(msg);
        } catch (Exception e) {
            System.err.println("EXCEPTION Mail.run - send failed, exception: " + e.getMessage() + "; " + to);
            if (e.getMessage() != null && !e.getMessage().equals("Invalid Addresses")) {
                e.printStackTrace();
            }
        }
    }

}
