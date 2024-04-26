package com.account.opening.system.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerRegistrationRes(
        @Schema(name = "username", description = "Account username", example = "username") String username,
        @Schema(name = "password", description = "Account password", example = "password") String password) {
}
