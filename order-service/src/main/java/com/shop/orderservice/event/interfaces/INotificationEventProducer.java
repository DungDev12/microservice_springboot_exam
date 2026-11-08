package com.shop.orderservice.event.interfaces;


import com.shop.orderservice.event.TransferNotificationEvent;

public interface INotificationEventProducer {
    void send(TransferNotificationEvent event, String TOPIC);
}
