package com.shop.notificationservice.service;

import com.shop.notificationservice.dto.events.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @KafkaListener(
            topics = "verify-account-events",
            groupId = "notification-service-group"
    )
    public void testMail(NotificationEvent event) {
        log.info(
                "Thông báo đến email nội dung là: {}",
                event.message()
        );
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com"); // Email của bạn (hoặc cấu hình chung)
        message.setTo("dungdev12@gmail.com");
        message.setSubject("Test");
        message.setText("Test tin nhắn");

        mailSender.send(message);
        System.out.println("Gửi mail thành công!");
    }
}
