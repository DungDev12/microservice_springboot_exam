package com.shop.identitycustomerservice.event;

import java.util.UUID;

public record NotificationEvent(
        UUID uuid,
        String toEmail
) implements KafkaEvent {
}
