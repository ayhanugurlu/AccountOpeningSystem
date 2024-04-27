package com.account.opening.system.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressDTO(
        @Schema(name = "postalCode", description = "postal code", example = "6666AB") String postalCode,
        @Schema(name = "addressLine1", description = "address", example = "Street1") String addressLine1,
        @Schema(name = "addressLine2", description = "address", example = "Street2") String addressLine2,
        @Schema(name = "city", description = "city", example = "Amsterdam") String city,
        @Schema(name = "country", description = "country code", example = "NL") String country) {
}
