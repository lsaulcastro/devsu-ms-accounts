package com.devsu.accounts.service;

import com.devsu.accounts.domain.exception.BusinessRuleViolationException;
import com.devsu.accounts.domain.exception.EntityNotFoundException;
import com.devsu.accounts.domain.model.Account;
import com.devsu.accounts.domain.model.CustomerView;
import com.devsu.accounts.dto.CreateAccountRequest;
import com.devsu.accounts.dto.UpdateAccountRequest;
import com.devsu.accounts.repository.AccountRepository;
import com.devsu.accounts.repository.CustomerViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountCommandService {

    private final AccountRepository accountRepository;
    private final CustomerViewRepository customerViewRepository;

    public Account create(CreateAccountRequest request) {
        ensureCustomerExistsAndActive(request.customerId());
        ensureAccountNumberIsAvailable(request.accountNumber());

        Account account = new Account(
                request.accountNumber(),
                request.accountType(),
                request.initialBalance(),
                true,
                request.customerId()
        );

        Account saved = accountRepository.save(account);
        log.info("Account created: accountNumber={}, customerId={}",
                saved.getAccountNumber(), saved.getCustomerId());
        return saved;
    }

    public Account update(String accountNumber, UpdateAccountRequest request) {
        Account account = findAccountOrFail(accountNumber);
        account.updateData(request.accountType(), request.active());
        Account saved = accountRepository.save(account);
        log.info("Account updated: accountNumber={}", saved.getAccountNumber());
        return saved;
    }

    public void deactivate(String accountNumber) {
        Account account = findAccountOrFail(accountNumber);
        account.deactivate();
        accountRepository.save(account);
        log.info("Account deactivated: accountNumber={}", accountNumber);
    }

    private void ensureCustomerExistsAndActive(String customerId) {
        CustomerView customer = customerViewRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer", customerId));

        if (!customer.isActive()) {
            throw new BusinessRuleViolationException(
                    "CUSTOMER_INACTIVE",
                    "Customer " + customerId + " is inactive and cannot have new accounts");
        }
    }

    private void ensureAccountNumberIsAvailable(String accountNumber) {
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new BusinessRuleViolationException(
                    "ACCOUNT_NUMBER_ALREADY_EXISTS",
                    "Account with number '" + accountNumber + "' already exists");
        }
    }

    private Account findAccountOrFail(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account", accountNumber));
    }
}