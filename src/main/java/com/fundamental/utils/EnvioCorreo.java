/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

import com.fundamental.services.Dao;
import com.fundamental.services.SvcMaintenance;
import com.sisintegrados.generic.bean.GenericSMTP;
import com.sisintegrados.generic.bean.UnoConfiguraciones;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author jescobar
 */
public class EnvioCorreo {

    SvcMaintenance main = new SvcMaintenance();
    GenericSMTP sm = new GenericSMTP();

//    public EnvioCorreo() {
//        try {
//            System.out.println("entra al envioCorreo");
//            for (GenericSMTP smtp : main.getSMTP()) {
//                System.out.println("smtp.getLlave().indexOf(\"smtp.host\") "+smtp.getLlave().indexOf("smtp.host"));
//                System.out.println("smtp.getValor() "+smtp.getValor());
//                
//                System.out.println("smtp.getLlave().indexOf(\"smtp.port\") "+smtp.getLlave().indexOf("smtp.port"));
//                System.out.println("smtp.getValor() "+smtp.getValor());
//                if (smtp.getLlave().indexOf("smtp.host") != -1) {
//                    sm.setHost(smtp.getValor());
//                }
//                if (smtp.getLlave().indexOf("smtp.port") != -1) {
//                    sm.setPort(smtp.getValor());
//                }
//                if (smtp.getLlave().indexOf("smtp.user") != -1) {
//                    sm.setUserAuth(smtp.getValor());
//                }
//                if (smtp.getLlave().indexOf("smtp.pass") != -1) {
//                    sm.setPassAuth(smtp.getValor());
//                }
//                if (smtp.getLlave().indexOf("smtp.tipo") != -1) {
//                    sm.setTipoSMTP(smtp.getValor());
//                }
//                if (smtp.getLlave().indexOf("smtp.mailfrom") != -1) {
//                    sm.setMailFrom(smtp.getValor());
//                }
//            }
//            
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public boolean enviarMail(String correoDestino, String mensaje, String subject) {
        boolean regresa = false;
//        String lsTiposeg = sm.getTipoSMTP();
        try {
            for (GenericSMTP smtp : main.getSMTP()) {
                System.out.println("smtp.getLlave().indexOf(\"smtp.host\") "+smtp.getLlave().indexOf("smtp.host"));
                System.out.println("smtp.getValor() "+smtp.getValor());
                
                System.out.println("smtp.getLlave().indexOf(\"smtp.port\") "+smtp.getLlave().indexOf("smtp.port"));
                System.out.println("smtp.getValor() "+smtp.getValor());
                if (smtp.getLlave().indexOf("smtp.host") != -1) {
                    sm.setHost(smtp.getValor());
                }
                if (smtp.getLlave().indexOf("smtp.port") != -1) {
                    sm.setPort(smtp.getValor());
                }
                if (smtp.getLlave().indexOf("smtp.user") != -1) {
                    sm.setUserAuth(smtp.getValor());
                }
                if (smtp.getLlave().indexOf("smtp.pass") != -1) {
                    sm.setPassAuth(smtp.getValor());
                }
                if (smtp.getLlave().indexOf("smtp.tipo") != -1) {
                    sm.setTipoSMTP(smtp.getValor());
                }
                if (smtp.getLlave().indexOf("smtp.mailfrom") != -1) {
                    sm.setMailFrom(smtp.getValor());
                }
            }
            
            
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            System.out.println("getHost "+sm.getHost());
            props.put("mail.smtp.host", sm.getHost());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", sm.getPort());
            props.put("mail.smtp.starttls.enable", "true");

//            if (lsTiposeg.equals("TLS")) {
//                props.put("mail.smtp.starttls.enable", "true");
//            } else {
////            SSL
//                props.put("mail.smtp.socketFactory.port", sm.getPort());
//                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            }

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sm.getUserAuth(), sm.getPassAuth());
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sm.getMailFrom()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(correoDestino));
            message.setSubject(subject);
            message.setContent(mensaje, "text/html");
            message.setSentDate(new Date());
            
            Transport.send(message);
            //Transport tr = session.getTransport("smtp");
            //tr.connect();
            //tr.send(message);
            //tr.close();
            session.getTransport().close();            
            regresa = true;
            //System.out.println("Done");

        }  catch (Exception ex){
            ex.printStackTrace();
        }

        return regresa;

    }
}
