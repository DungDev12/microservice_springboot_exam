package com.shop.identitycustomerservice.event;

import com.shop.identitycustomerservice.event.interfaces.INotificationEventProducer;
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
    public void send(NotificationEvent event, String TOPIC) {
        kafkaTemplate.send(TOPIC, event.uuid().toString(), event)
              .whenComplete((res, ex) -> {
                  if (ex != null) {
                      log.error(
                              "Failed to send notification event: {}",
                              event.uuid(),
                              ex
                      );
                  }
              });
    }

    @Override
    public void sendVerificationCode(OtpNotificationEvent event, String TOPIC) {
        kafkaTemplate.send(TOPIC, event.uuid().toString(), event)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error(
                                "Failed to send notification event: {}",
                                event.uuid(),
                                ex
                        );
                        return;
                    }
                    var metadata = res.getRecordMetadata();

                    log.info(
                            "Order event sent: topic={}, partition={}, offset={}",
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset()
                    );
                });
    }
}
