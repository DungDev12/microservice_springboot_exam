package com.shop.identitycustomerservice.event.interfaces;

import com.shop.identitycustomerservice.event.NotificationEvent;

public interface INotificationEventProducer {
    void send(NotificationEvent notificationEvent);
    void sendVerificationCode(NotificationEvent notificationEvent);
}
