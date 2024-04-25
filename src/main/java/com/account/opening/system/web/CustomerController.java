package com.account.opening.system.web;

import com.account.opening.system.service.CustomerService;
import com.account.opening.system.service.dto.response.BankAccountOverviewResp;
import com.account.opening.system.service.dto.request.CustomerCreateReq;
import com.account.opening.system.service.dto.response.UserRegistrationRes;
import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public UserRegistrationRes createCustomer(@RequestBody CustomerCreateReq customerDTO) {
        return customerService.createCustomer(customerDTO);
    }

    @GetMapping("/overview/{username}")
    @PreAuthorize("hasRole('USER')")
    public List<BankAccountOverviewResp> getCustomerOverview(@PathVariable String username) {
        return customerService.getCustomerOverview(username);
    }
}
