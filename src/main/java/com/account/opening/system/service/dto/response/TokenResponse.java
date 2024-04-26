package com.account.opening.system.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(@Schema(name = "username", description = "Account username", example = "username")String username,
                            @Schema(name = "token", description = "bearer token", example = "asdasdasdasdasdasdasd")String token) {
}
