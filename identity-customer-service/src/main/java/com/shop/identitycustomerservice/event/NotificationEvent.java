package com.shop.identitycustomerservice.event;

import lombok.*;

import java.util.UUID;

@Data
@Builder
public class NotificationEvent {
    private UUID uuid;
    private String message;
}
