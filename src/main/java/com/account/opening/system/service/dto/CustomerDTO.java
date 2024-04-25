package com.account.opening.system.service.dto;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record CustomerDTO(String name, String username, @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
                          AddressDTO addressDTO, String documentId) {
}
