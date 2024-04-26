package com.account.opening.system.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LogonRequest(
        @Schema(name = "token", description = "Token getting from token endpoint", example = "token") String token) {
}
