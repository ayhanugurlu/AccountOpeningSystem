package com.account.opening.system.service;

import com.account.opening.system.service.dto.response.BankAccountOverviewResp;
import com.account.opening.system.service.dto.request.CustomerCreateReq;
import com.account.opening.system.service.dto.response.UserRegistrationRes;

import java.util.List;

public interface CustomerService {

    /**
     * Create a new customer
     *
     * @param customerDTO
     * @return iban
     */
    UserRegistrationRes createCustomer(CustomerCreateReq customerDTO);

    /**
     * Get customer overview
     *
     * @param username
     * @return customer overview
     */
    List<BankAccountOverviewResp> getCustomerOverview(String username);

}
