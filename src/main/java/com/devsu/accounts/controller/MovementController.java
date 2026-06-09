package com.devsu.accounts.controller;

import com.devsu.accounts.dto.CreateMovementRequest;
import com.devsu.accounts.dto.MovementResponse;
import com.devsu.accounts.dto.common.ApiResponse;
import com.devsu.accounts.mapper.MovementMapper;
import com.devsu.accounts.service.MovementCommandService;
import com.devsu.accounts.service.MovementQueryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts/{accountNumber}/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementCommandService commandService;
    private final MovementQueryService queryService;
    private final MovementMapper movementMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<MovementResponse>> register(
            @PathVariable String accountNumber,
            @Valid @RequestBody CreateMovementRequest request,
            HttpServletRequest httpRequest) {
        MovementResponse response = movementMapper.toResponse(
                commandService.register(accountNumber, request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), httpRequest.getRequestURI(), response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovementResponse>>> findByAccount(
            @PathVariable String accountNumber,
            HttpServletRequest httpRequest) {
        List<MovementResponse> response = movementMapper.toResponseList(
                queryService.findByAccountNumber(accountNumber));
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), httpRequest.getRequestURI(), response));
    }
}