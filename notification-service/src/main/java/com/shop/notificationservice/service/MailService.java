package com.shop.notificationservice.service;

import com.shop.notificationservice.dto.events.NotificationEvent;
import com.shop.notificationservice.dto.events.OtpNotificationEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @KafkaListener(
            topics = "otp-customer-account-events",
            groupId = "notification-service-group"
    )
    public void verifyCustomerAccountEvent(OtpNotificationEvent event) {
        log.info("Nhận sự kiện gửi mã OTP email cho: {}", event.toEmail());
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            // 1. Đẩy dữ liệu động vào context của Thymeleaf
            Context context = new Context();
            // Lấy mã OTP từ event truyền vào (Ví dụ giả định trường này là event.getOtp())
            context.setVariable("ttl", formatTtlTime(event.ttl()));
            context.setVariable("otpCode", event.otp());

            // 2. Render nội dung HTML từ file template
            String htmlContent = templateEngine.process("otp-template", context);

            // 3. Cấu hình các thông tin gửi mail cơ bản
            helper.setFrom("saigonithcm.vn@gmail.com", "Shop Online");
            helper.setTo(event.toEmail()); // Điền email người nhận từ event
            helper.setSubject("Mã OTP xác thực tài khoản của bạn");
            helper.setText(htmlContent, true); // Đặt tham số thứ hai là TRUE để xác nhận gửi dạng HTML

            // 4. Thực thi gửi mail đi
            mailSender.send(message);
            System.out.println("Gửi mail thành công!");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gửi email template HTML: " + e.getMessage());
        }

    }

    @KafkaListener(
            topics = "hello-new-customer-events",
            groupId = "notification-service-group"
    )
    public void helloNewCustomerMail(NotificationEvent event){
        log.info("Nhận sự kiện chào người mới email cho: {}", event.toEmail());
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            // 1. Đẩy dữ liệu động vào context của Thymeleaf
            Context context = new Context();
            // Lấy mã OTP từ event truyền vào (Ví dụ giả định trường này là event.getOtp())
            // 2. Render nội dung HTML từ file template
            String htmlContent = templateEngine.process("hello-new-customer-template", context);

            // 3. Cấu hình các thông tin gửi mail cơ bản
            helper.setFrom("saigonithcm.vn@gmail.com", "Shop Online");
            helper.setTo(event.toEmail()); // Điền email người nhận từ event
            helper.setSubject("Kính chào quý khách");
            helper.setText(htmlContent, true); // Đặt tham số thứ hai là TRUE để xác nhận gửi dạng HTML

            // 4. Thực thi gửi mail đi
            mailSender.send(message);
            System.out.println("Gửi mail thành công!");
        } catch (Exception e){
            throw new RuntimeException("Lỗi khi gửi email template HTML: " + e.getMessage());
        }
    }

    private String formatTtlTime(long ttl) {

        // Chuyển số sang Duration dựa trên số giây
        Duration duration = Duration.ofSeconds(ttl);

        // Lấy ra các thành phần trực tiếp từ thư viện
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        if (days > 0) {
            return hours > 0
                    ? "%d ngày %d giờ".formatted(days, hours)
                    : "%d ngày".formatted(days);
        }

        if (hours > 0) {
            return minutes > 0
                    ? "%d giờ %d phút".formatted(hours, minutes)
                    : "%d giờ".formatted(hours);
        }

        if (minutes > 0) {
            return seconds > 0
                    ? "%d phút %d giây".formatted(minutes, seconds)
                    : "%d phút".formatted(minutes);
        }

        return "%d giây".formatted(seconds);
    }
}
