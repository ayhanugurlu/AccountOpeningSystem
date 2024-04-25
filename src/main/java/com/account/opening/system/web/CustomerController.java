package com.account.opening.system.web;

import com.account.opening.system.service.CustomerService;
import com.account.opening.system.service.dto.response.BankAccountOverviewResp;
import com.account.opening.system.service.dto.request.CustomerCreateReq;
import com.account.opening.system.service.dto.response.UserRegistrationRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public UserRegistrationRes createCustomer(@RequestBody CustomerCreateReq customerDTO) {
        return customerService.createCustomer(customerDTO);
    }

    @PostMapping("/overview/{username}")
    public List<BankAccountOverviewResp> getCustomerOverview(@PathVariable String username) {
        return customerService.getCustomerOverview(username);
    }
}
