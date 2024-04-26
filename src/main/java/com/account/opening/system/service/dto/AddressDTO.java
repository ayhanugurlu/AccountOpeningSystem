package com.account.opening.system.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressDTO(
        @Schema(name = "postal code", description = "postal code", example = "6666AB") String postalCode,
        @Schema(name = "address", description = "address", example = "Street") String addressLine1,
        @Schema(name = "address", description = "address", example = "Street") String addressLine2,
        @Schema(name = "city", description = "city", example = "Amsterdam") String city,
        @Schema(name = "country code", description = "country code", example = "NL") String country) {
}
