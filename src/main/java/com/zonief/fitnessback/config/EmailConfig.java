package com.zonief.fitnessback.config;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EmailConfig {

  @Value("${email.username}")
  private String username;
  @Value("${email.password}")
  private String password;
  @Value("${email.smtp.host}")
  private String host;
  @Value("${email.smtp.ssl.trust}")
  private String sslTrust;
  @Value("${email.smtp.port}")
  private String port;

  @Bean(name = "emailProperties")
  public Properties properties() {
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", true);
    properties.put("mail.smtp.starttls.enable", true);
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", port);
    properties.put("mail.smtp.ssl.trust", sslTrust);
    return properties;
  }

  @Bean(name = "messageProperties")
  public Message message() {

    Session session = Session.getInstance(properties(), new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    Message message = new MimeMessage(session);
    try {
      message.setFrom(new InternetAddress("from@gmail.com"));
      message.setSubject("Mail Subject");

      String msg = "This is my first email using JavaMailer";

      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(mimeBodyPart);
    } catch (MessagingException e) {
      log.error("Error while building the email message: {}", e.getMessage());
    }

    return message;

  }

}
