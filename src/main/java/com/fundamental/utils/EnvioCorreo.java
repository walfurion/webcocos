/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

import com.fundamental.services.Dao;
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

    UnoConfiguraciones obj;
    Dao dao ;
    private String host;
    private String port;
    private String tipoSMTP;
    private String userAuth;
    private String passAuth;
    private String mailFrom;

    public EnvioCorreo() {
        try {
//            for (UnoConfiguraciones uc : dao.getConfiguracionesSMTP()) {
//                if (uc.getLlave().indexOf("smtp.host") != -1) {
//                    setHost(uc.getValor());
//                }
//                if (uc.getLlave().indexOf("smtp.port") != -1) {
//                    setPort(uc.getValor());
//                }
//                if (uc.getLlave().indexOf("smtp.user") != -1) {
//                    setUserAuth(uc.getValor());
//                }
//                if (uc.getLlave().indexOf("smtp.pass") != -1) {
//                    setPassAuth(uc.getValor());
//                }
//                if (uc.getLlave().indexOf("smtp.tipo") != -1) {
//                    setTipoSMTP(uc.getValor());
//                }
//                if (uc.getLlave().indexOf("smtp.mailfrom") != -1) {
//                    setMailFrom(uc.getValor());
//                }
//            }
            

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTipoSMTP() {
        return tipoSMTP;
    }

    public void setTipoSMTP(String tipoSMTP) {
        this.tipoSMTP = tipoSMTP;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getPassAuth() {
        return passAuth;
    }

    public void setPassAuth(String passAuth) {
        this.passAuth = passAuth;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public boolean enviarMail(String correoDestino, String mensaje, String subject) {
        boolean regresa = false;
        String lsTiposeg = this.tipoSMTP;
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", this.host);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", this.port);
            props.put("mail.smtp.starttls.enable", "true");

//            if (lsTiposeg.equals("TLS")) {
//                props.put("mail.smtp.starttls.enable", "true");
//            } else {
////            SSL
//                props.put("mail.smtp.socketFactory.port", this.port);
//                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            }

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(getUserAuth(), getPassAuth());
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.mailFrom));
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
