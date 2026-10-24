package com.shop.identitycustomerservice.event;


import java.util.UUID;

public record OtpNotificationEvent(
        UUID uuid,
        String toEmail,
        String otp,
        int ttl
) implements KafkaEvent {
}
