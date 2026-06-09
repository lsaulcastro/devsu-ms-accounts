package com.devsu.accounts.controller;

import com.devsu.accounts.dto.AccountResponse;
import com.devsu.accounts.dto.CreateAccountRequest;
import com.devsu.accounts.dto.UpdateAccountRequest;
import com.devsu.accounts.dto.common.ApiResponse;
import com.devsu.accounts.mapper.AccountMapper;
import com.devsu.accounts.service.AccountCommandService;
import com.devsu.accounts.service.AccountQueryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountCommandService commandService;
    private final AccountQueryService queryService;
    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> create(
            @Valid @RequestBody CreateAccountRequest request,
            HttpServletRequest httpRequest) {
        AccountResponse response = accountMapper.toResponse(commandService.create(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{accountNumber}")
                .buildAndExpand(response.accountNumber())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), httpRequest.getRequestURI(), response));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> findByAccountNumber(
            @PathVariable String accountNumber,
            HttpServletRequest httpRequest) {
        AccountResponse response = accountMapper.toResponse(queryService.findByAccountNumber(accountNumber));
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), httpRequest.getRequestURI(), response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> findAll(HttpServletRequest httpRequest) {
        List<AccountResponse> response = accountMapper.toResponseList(queryService.findAll());
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), httpRequest.getRequestURI(), response));
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> update(
            @PathVariable String accountNumber,
            @Valid @RequestBody UpdateAccountRequest request,
            HttpServletRequest httpRequest) {
        AccountResponse response = accountMapper.toResponse(commandService.update(accountNumber, request));
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), httpRequest.getRequestURI(), response));
    }

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable String accountNumber) {
        commandService.deactivate(accountNumber);
    }
}