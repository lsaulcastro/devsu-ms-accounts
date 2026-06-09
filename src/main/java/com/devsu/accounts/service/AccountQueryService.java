package com.devsu.accounts.service;

import com.devsu.accounts.domain.exception.EntityNotFoundException;
import com.devsu.accounts.domain.model.Account;
import com.devsu.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountQueryService {

    private final AccountRepository accountRepository;

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account", accountNumber));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public List<Account> findByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }
}