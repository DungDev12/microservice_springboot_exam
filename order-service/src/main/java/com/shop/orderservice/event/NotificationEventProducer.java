package com.shop.orderservice.event;

import com.shop.orderservice.event.interfaces.INotificationEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventProducer implements INotificationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public void send(TransferNotificationEvent event, String TOPIC) {
        kafkaTemplate.send(TOPIC, event.uuidCustomer().toString(), event)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error(
                                "Failed to send notification event: {}",
                                event.uuidCustomer(),
                                ex
                        );
                    }
                });
    }
}
