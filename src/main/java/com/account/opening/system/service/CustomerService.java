package com.account.opening.system.service;

import com.account.opening.system.service.dto.CustomerDTO;

public interface CustomerService {

    /**
     * Create a new customer
     * @param customerDTO
     * @return iban
     */
    String createCustomer(CustomerDTO customerDTO);

    /**
     * Get customer by iban
     * @param iban
     * @return customerDTO
     */
    CustomerDTO getCustomer(String iban);
}
