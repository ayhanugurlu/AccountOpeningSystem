package com.account.opening.system.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record BankAccountOverviewResp(@Schema(name = "Account number", description = "Account number", example = "123")Long accountNumber, String iban, String accountType, String currency, Double balance, String status) {
}
