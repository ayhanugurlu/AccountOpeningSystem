package com.account.opening.system.service.dto.request;


import com.account.opening.system.service.dto.AddressDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record CustomerRegistrationReq(
        @Schema(name = "name", description = "Account owner name", example = "joe") String name,
        @Schema(name = "username", description = "Account owner username", example = "joe.dash") String username,
        @Schema(name = "dateOfBirth", description = "date of birth", example = "2000-04-25T21:32:21.960Z") @JsonFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
        AddressDTO addressDTO,
        @Schema(name = "documentId", description = "document id", example = "A124") String documentId,
        @Schema(name = "currency", description = "currency", example = "EUR")String currency,
        @Schema(name = "status", description = "status", example = "ACTIVE")String status,
        @Schema(name = "accountType", description = "account type", example = "SAVING") String accountType,
        @Schema(name = "balance", description = "balance", example = "101.5") Double balance) {
}
