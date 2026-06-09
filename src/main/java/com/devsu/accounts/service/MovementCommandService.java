package com.devsu.accounts.service;

import com.devsu.accounts.domain.exception.BusinessRuleViolationException;
import com.devsu.accounts.domain.exception.EntityNotFoundException;
import com.devsu.accounts.domain.model.Account;
import com.devsu.accounts.domain.model.CustomerView;
import com.devsu.accounts.domain.model.Movement;
import com.devsu.accounts.dto.CreateMovementRequest;
import com.devsu.accounts.repository.AccountRepository;
import com.devsu.accounts.repository.CustomerViewRepository;
import com.devsu.accounts.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MovementCommandService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final CustomerViewRepository customerViewRepository;

    public Movement register(String accountNumber, CreateMovementRequest request) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account", accountNumber));

        ensureCustomerIsActive(account.getCustomerId());

        Movement movement = new Movement(request.movementType(), request.amount());
        account.apply(movement);

        accountRepository.save(account);
        Movement saved = movementRepository.save(movement);

        log.info("Movement registered: accountNumber={}, type={}, amount={}, newBalance={}",
                accountNumber, movement.getMovementType(), movement.getAmount(),
                account.getCurrentBalance());

        return saved;
    }

    private void ensureCustomerIsActive(String customerId) {
        CustomerView customer = customerViewRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer", customerId));

        if (!customer.isActive()) {
            throw new BusinessRuleViolationException(
                    "CUSTOMER_INACTIVE",
                    "Customer " + customerId + " is inactive; movements cannot be registered");
        }
    }
}