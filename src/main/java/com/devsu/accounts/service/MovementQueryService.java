package com.devsu.accounts.service;

import com.devsu.accounts.domain.model.Account;
import com.devsu.accounts.domain.model.Movement;
import com.devsu.accounts.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovementQueryService {

    private final AccountQueryService accountQueryService;
    private final MovementRepository movementRepository;

    public List<Movement> findByAccountNumber(String accountNumber) {
        Account account = accountQueryService.findByAccountNumber(accountNumber);
        return movementRepository.findByAccountIdOrderByDateDesc(account.getId());
    }
}