package com.account.opening.system.service.dto.response;

public record BankAccountOverviewResp(Long accountNumber, String iban, String accountType, String currency, Double balance, String status) {
}
