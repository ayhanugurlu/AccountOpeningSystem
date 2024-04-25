package com.account.opening.system.service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record CustomerDTO(String name, String username, @JsonFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
                          AddressDTO addressDTO, String documentId) {
}
