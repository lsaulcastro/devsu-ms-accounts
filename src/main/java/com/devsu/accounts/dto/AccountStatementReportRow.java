package com.devsu.accounts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AccountStatementReportRow(

        @JsonProperty("fecha")
        String date,

        @JsonProperty("cliente")
        String customerName,

        @JsonProperty("numeroCuenta")
        String accountNumber,

        @JsonProperty("tipo")
        String accountType,

        @JsonProperty("saldoInicial")
        BigDecimal initialBalance,

        @JsonProperty("estado")
        boolean active,

        @JsonProperty("movimiento")
        BigDecimal movement,

        @JsonProperty("saldoDisponible")
        BigDecimal availableBalance
) {
}