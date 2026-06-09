package com.devsu.accounts.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerEventConsumer {

    private final CustomerEventHandler eventHandler;

    @KafkaListener(
            topics = "${app.kafka.topic.customer-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CustomerEvent event) {
        try {
            eventHandler.handle(event);
        } catch (Exception ex) {
            log.error("Failed to process CustomerEvent: customerId={}, type={}",
                    event != null ? event.customerId() : "null",
                    event != null ? event.eventType() : "null",
                    ex);
            throw ex;
        }
    }
}