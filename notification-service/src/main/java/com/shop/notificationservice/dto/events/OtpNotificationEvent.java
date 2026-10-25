package com.shop.notificationservice.dto.events;


import java.util.UUID;

public record OtpNotificationEvent(
        UUID uuid,
        String toEmail,
        String otp,
        long ttl
)  {
}
