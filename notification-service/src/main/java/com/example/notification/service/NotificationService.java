package com.example.notification.service;

import com.example.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "order-placed")
    public void listen(OrderPlacedEvent orderPlacedEvent) {
        // Simulate sending a notification
        log.info("Got Message from order-placed Topic: {}", orderPlacedEvent);
        //send message to email
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springshop@email.com");
            messageHelper.setTo(orderPlacedEvent.getEmail().toString());
            messageHelper.setSubject("Thank you for your order, " + orderPlacedEvent.getEmail() + ". Your order number is " + orderPlacedEvent.getOrderNumber() + ".");
            messageHelper.setText(String.format("""
                    Hi %s,%s
                    
                    Your order with number %s has been placed successfully.
                    
                    Best regards,
                    Spring shop.
                    """,
                    orderPlacedEvent.getFirstName().toString(),
                    orderPlacedEvent.getLastName().toString(),
                    orderPlacedEvent.getOrderNumber()));
        };

        //send email
        try {
            javaMailSender.send(messagePreparator);
            log.info("Email sent to {}", orderPlacedEvent.getEmail());
        } catch (MailException e) {
            log.error("Error sending email to {}: {}", orderPlacedEvent.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send email notification", e);
        }
    }
}
