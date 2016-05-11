/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Classe responsavel por enviar emails.
 * @author Maycon
 */
public class EmailSender implements Serializable{

    private static final String USERNAME = "Maycon@gmail.com";
    private static final String PASSWORD = "mudar@2014";
    private Properties props;
    private final Session session;

    public EmailSender() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
    }

    public void sendEmailReset(String name, String email, String token, Date data) {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Resetar Password");
            message.setText("Olá," + name
                    + "\n\n No dia " + new SimpleDateFormat("dd/MM/YYYY HH:ss").format(data) + " foi solicitado a alteração de senha de sua conta no sistema de REFLORESTAMENTO."
                    + "\nPara prosseguir entre em recuperar senha no sistema e insira o código: " + token
                    + "\n\nCaso não tenha solicitado, desconsidere esse e-mail.");
            Transport.send(message);
        } catch (MessagingException e) {
        }
    }

}
