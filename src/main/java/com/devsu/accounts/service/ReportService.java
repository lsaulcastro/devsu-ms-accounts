package com.devsu.accounts.service;

import com.devsu.accounts.domain.exception.EntityNotFoundException;
import com.devsu.accounts.domain.exception.InvalidDataException;
import com.devsu.accounts.domain.model.Account;
import com.devsu.accounts.domain.model.CustomerView;
import com.devsu.accounts.domain.model.Movement;
import com.devsu.accounts.dto.AccountStatementReportRow;
import com.devsu.accounts.repository.AccountRepository;
import com.devsu.accounts.repository.CustomerViewRepository;
import com.devsu.accounts.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private static final ZoneId REPORT_ZONE = ZoneId.of("America/Guayaquil");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");

    private final CustomerViewRepository customerViewRepository;
    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    public List<AccountStatementReportRow> generate(String customerId, LocalDate fromDate, LocalDate toDate) {
        validateDateRange(fromDate, toDate);

        CustomerView customer = customerViewRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer", customerId));

        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        if (accounts.isEmpty()) {
            log.info("No accounts found for customerId={}, returning empty report", customerId);
            return List.of();
        }

        Map<Long, Account> accountById = accounts.stream()
                .collect(Collectors.toMap(Account::getId, account -> account));

        Instant fromInstant = fromDate.atStartOfDay(REPORT_ZONE).toInstant();
        Instant toInstant = toDate.plusDays(1).atStartOfDay(REPORT_ZONE).toInstant();

        List<Movement> movements = movementRepository.findByAccountIdsAndDateRange(
                List.copyOf(accountById.keySet()), fromInstant, toInstant);

        log.info("Generating report for customerId={}: {} accounts, {} movements in range",
                customerId, accounts.size(), movements.size());

        return movements.stream()
                .map(movement -> toReportRow(movement, accountById.get(movement.getAccountId()), customer))
                .toList();
    }

    private AccountStatementReportRow toReportRow(Movement movement, Account account, CustomerView customer) {
        return new AccountStatementReportRow(
                formatDate(movement.getDate()),
                customer.getName(),
                account.getAccountNumber(),
                account.getAccountType().toSpanish(),
                account.getInitialBalance(),
                account.isActive(),
                movement.getMovementType().applyTo(movement.getAmount()),
                movement.getBalance()
        );
    }

    private String formatDate(Instant instant) {
        return instant.atZone(REPORT_ZONE).format(DATE_FORMATTER);
    }

    private void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            throw new InvalidDataException("Both fechaInicio and fechaFin are required");
        }
        if (fromDate.isAfter(toDate)) {
            throw new InvalidDataException("fechaInicio cannot be after fechaFin");
        }
    }
}