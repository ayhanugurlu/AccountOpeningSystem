package com.account.opening.system.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record BankAccountOverviewResp(@Schema(name = "accountNumber", description = "Account number", example = "123")Long accountNumber,
                                      @Schema(name = "iban", description = "iban", example = "NL58ABNA0000000001")String iban,
                                      @Schema(name = "accountType", description = "Account Type", example = "SAVING")String accountType,
                                      @Schema(name = "currency", description = "Currency", example = "EUR")String currency,
                                      @Schema(name = "balance", description = "balance", example = "10.0")Double balance,
                                      @Schema(name = "status", description = "status", example = "ACTIVE")String status) {
}
