package com.zonief.fitnessback.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

  public final Message message;

  public void sendEmail(String messageContent, String emailAddressTo) {
    try {
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddressTo));
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(messageContent, "text/html; charset=utf-8");
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(mimeBodyPart);
      message.setContent(multipart);
    } catch (MessagingException e) {
      log.error("Error while sending email: {}", e.getMessage());
    }

  }

}
