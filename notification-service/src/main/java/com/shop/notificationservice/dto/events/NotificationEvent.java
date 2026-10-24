package com.shop.notificationservice.dto.events;

import java.util.UUID;


public record NotificationEvent(
        UUID uuid,
        String toEmail
) {
}
