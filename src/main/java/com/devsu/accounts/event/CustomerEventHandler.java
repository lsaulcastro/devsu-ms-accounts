package com.devsu.accounts.event;

import com.devsu.accounts.domain.model.CustomerView;
import com.devsu.accounts.repository.CustomerViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerEventHandler {

    private final CustomerViewRepository customerViewRepository;

    @Transactional
    public void handle(CustomerEvent event) {
        log.debug("Handling event: type={}, customerId={}",
                event.eventType(), event.customerId());

        switch (event.eventType()) {
            case CREATED, UPDATED -> handleCreatedOrUpdated(event);
            case DEACTIVATED -> handleDeactivated(event);
        }
    }

    private void handleCreatedOrUpdated(CustomerEvent event) {
        customerViewRepository.findById(event.customerId())
                .ifPresentOrElse(
                        existing -> {
                            existing.updateFrom(event.name(), event.identification(), event.active());
                            log.info("CustomerView updated: customerId={}", event.customerId());
                        },
                        () -> {
                            CustomerView newView = new CustomerView(
                                    event.customerId(),
                                    event.name(),
                                    event.identification(),
                                    event.active()
                            );
                            customerViewRepository.save(newView);
                            log.info("CustomerView created: customerId={}", event.customerId());
                        }
                );
    }

    private void handleDeactivated(CustomerEvent event) {
        customerViewRepository.findById(event.customerId())
                .ifPresentOrElse(
                        existing -> {
                            existing.markDeactivated();
                            log.info("CustomerView deactivated: customerId={}", event.customerId());
                        },
                        () -> log.warn(
                                "DEACTIVATED event received for unknown customerId={}, ignoring",
                                event.customerId())
                );
    }
}