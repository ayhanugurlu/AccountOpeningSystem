package com.account.opening.system.web;

import com.account.opening.system.service.CustomerService;
import com.account.opening.system.service.dto.response.BankAccountOverviewResp;
import com.account.opening.system.service.dto.request.CustomerRegistrationReq;
import com.account.opening.system.service.dto.response.CustomerRegistrationRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Register a new customer")
    @PostMapping("/register")
    public ResponseEntity<CustomerRegistrationRes> createCustomer(@RequestBody CustomerRegistrationReq customerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customerDTO));
    }

    @GetMapping("/overview/{username}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BankAccountOverviewResp>> getCustomerOverview(@PathVariable String username) {
        return ResponseEntity.ok().body(customerService.getCustomerOverview(username));
    }
}
