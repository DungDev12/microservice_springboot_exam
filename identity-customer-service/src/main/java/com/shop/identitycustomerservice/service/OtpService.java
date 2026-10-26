package com.shop.identitycustomerservice.service;

import com.shop.identitycustomerservice.dto.request.OtpVerifyRequest;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.event.NotificationEvent;
import com.shop.identitycustomerservice.event.NotificationEventProducer;
import com.shop.identitycustomerservice.event.OtpNotificationEvent;
import com.shop.identitycustomerservice.service.interfaces.IOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class OtpService implements IOtpService {

    private final StringRedisTemplate redisTemplate;

    private final NotificationEventProducer notificationEventProducer;

    private final SecureRandom secureRandom  = new SecureRandom();
    private final String secret;
    private final long ttlSeconds;


    public OtpService(
            StringRedisTemplate redisTemplate,
            @Value("${otp.secret}") String secret,
            @Value("${otp.ttl-seconds:300}") long ttlSeconds,
            @Value("${otp.max-attempts:5}") int maxAttempts,
            NotificationEventProducer notificationEventProducer
    ) {
        this.redisTemplate = redisTemplate;
        this.secret = secret;
        this.ttlSeconds = ttlSeconds;
        this.notificationEventProducer = notificationEventProducer;
    }

    @Override
    public String generateOtp(String accountId) {
        // Tạo OTP
        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        String key = otpKey(accountId);
        String otpHash = hmac(accountId + ":" + otp);

        redisTemplate.opsForValue().set(
                key,
                otpHash,
                ttlSeconds,
                TimeUnit.SECONDS
        );

        redisTemplate.delete(attemptKey(accountId));

        return otp;
    }

    @Override
    public void generateOtpAndSendEmail(CustomerAccount account) {
        String otp = generateOtp(account.getUuid().toString());

        notificationEventProducer.sendVerificationCode(
             new OtpNotificationEvent(
                     account.getUuid(),
                     account.getEmail(),
                     otp,
                     ttlSeconds
             ),
                "otp-customer-account-events"
        );
    }

    @Override
    public boolean verifyOtp(Map<String, String> account, OtpVerifyRequest request) {
        String key = otpKey(account.get("uuid"));
        String attemptKey = attemptKey(account.get("uuid"));

        // Lấy hash OTP từ Redis
        String otpHash = redisTemplate.opsForValue().get(key);


        // OTP không tồn tại ( hết hạn do ttl )
        if (otpHash == null) {
            return false;
        }

        // Hash OTP người dùng nhập
        String inputHash = hmac(account.get("uuid") + ":" + request.otp());

        // Check otp đã hash
        boolean matches = MessageDigest.isEqual(
                otpHash.getBytes(StandardCharsets.UTF_8),
                inputHash.getBytes(StandardCharsets.UTF_8)
        );
        // OTP không khớp
        if(!matches) {
            return false;
        }

        // Khớp thì xoá và gửi kết quả true
        redisTemplate.delete(key);
        redisTemplate.delete(attemptKey);
        return true;
    }

    private String otpKey(String accountId){
        return "otp:verify:" + accountId;
    }

    private String attemptKey(String accountId){
        return "otp:attempts:" + accountId;
    }

    private String hmac(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            mac.init(new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            ));

            return HexFormat.of().formatHex(
                    mac.doFinal(value.getBytes(StandardCharsets.UTF_8))
            );
        } catch (Exception ex) {
            throw new IllegalStateException("Không thể tạo HMAC", ex);
        }
    }
}
