package com.shop.identitycustomerservice.event.interfaces;

import com.shop.identitycustomerservice.event.NotificationEvent;
import com.shop.identitycustomerservice.event.OtpNotificationEvent;

public interface INotificationEventProducer {
    void send(NotificationEvent event, String TOPIC);
    void sendVerificationCode(OtpNotificationEvent event, String TOPIC);
}
