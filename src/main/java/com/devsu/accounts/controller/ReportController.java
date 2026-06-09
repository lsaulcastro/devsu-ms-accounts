package com.devsu.accounts.controller;

import com.devsu.accounts.dto.AccountStatementReportRow;
import com.devsu.accounts.dto.common.ApiResponse;
import com.devsu.accounts.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountStatementReportRow>>> getAccountStatement(
            @RequestParam("clienteId") String clienteId,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            HttpServletRequest httpRequest) {

        List<AccountStatementReportRow> rows = reportService.generate(clienteId, fechaInicio, fechaFin);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), httpRequest.getRequestURI(), rows));
    }
}