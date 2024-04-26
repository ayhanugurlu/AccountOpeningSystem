package com.account.opening.system.service;

import com.account.opening.system.service.dto.request.CustomerRegistrationReq;
import com.account.opening.system.service.dto.response.BankAccountOverviewResp;
import com.account.opening.system.service.dto.response.CustomerRegistrationRes;

import java.util.List;

/**
 * CustomerService interface
 **/
public interface CustomerService {

    /**
     * Create a new customer
     *
     * @param customerDTO
     * @return iban
     */
    CustomerRegistrationRes createCustomer(CustomerRegistrationReq customerDTO);

    /**
     * Get customer overview
     *
     * @param username
     * @return customer overview
     */
    List<BankAccountOverviewResp> getCustomerOverview(String username);

}
