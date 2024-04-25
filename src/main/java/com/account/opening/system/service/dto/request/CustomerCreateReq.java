package com.account.opening.system.service.dto.request;


import com.account.opening.system.service.dto.AddressDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record CustomerCreateReq(String name, String username, @JsonFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
                                AddressDTO addressDTO, String documentId, String currency, String status,
                                String accountType,
                                Double balance) {
}
