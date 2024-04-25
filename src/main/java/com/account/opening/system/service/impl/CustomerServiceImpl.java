package com.account.opening.system.service.impl;

import com.account.opening.system.data.BankAccountRepository;
import com.account.opening.system.data.CustomerRepository;
import com.account.opening.system.data.model.Address;
import com.account.opening.system.data.model.BankAccount;
import com.account.opening.system.data.model.Customer;
import com.account.opening.system.exception.BankAccountNotFoundException;
import com.account.opening.system.exception.IllegalCountryException;
import com.account.opening.system.exception.UsernameAlreadyExists;
import com.account.opening.system.service.CustomerService;
import com.account.opening.system.service.dto.request.CustomerCreateReq;
import com.account.opening.system.service.dto.response.BankAccountOverviewResp;
import com.account.opening.system.service.dto.response.UserRegistrationRes;
import com.account.opening.system.util.BankAccountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final BankAccountRepository bAnkAccountRepository;

    @Value("${allowed.countries:NL}")
    String[] allowedCountries;

    @Value("${bank.code:ABNA}")
    String bankCode;


    @Value("${bank.account.length:10}")
    int bankAccountLength = 0;

    @Override
    public UserRegistrationRes createCustomer(CustomerCreateReq customerDTO) {

        if (Arrays.stream(allowedCountries).noneMatch(country -> country.equals(customerDTO.addressDTO().country()))) {
            throw new IllegalCountryException("Country not allowed");
        }
        if (customerRepository.findByUsername(customerDTO.username()).isPresent()) {
            throw new UsernameAlreadyExists("Username already exists");
        }

        Address address = Address.builder().country(customerDTO.addressDTO()
                        .country()).city(customerDTO.addressDTO().city())
                .build();
        BankAccount bankAccount = BankAccount.builder()
                .build();
        Customer customer = Customer.builder()
                .name(customerDTO.name())
                .username(customerDTO.username())
                .dateOfBirth(customerDTO.dateOfBirth())
                .documentId(customerDTO.documentId())
                .password(BankAccountUtil.generateRandomPassword())
                .bankAccounts(Arrays.asList(bankAccount))
                .address(address).build();
        customer = customerRepository.save(customer);
        bankAccount = customer.getBankAccounts().stream().findFirst().orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        bankAccount.setIban(BankAccountUtil.generateIBAN(customerDTO.addressDTO().country(), bankCode, bankAccount.getId(), bankAccountLength));
        bAnkAccountRepository.save(bankAccount);
        return new UserRegistrationRes(customer.getUsername(), customer.getPassword());
    }

    @Override
    public List<BankAccountOverviewResp> getCustomerOverview(String username) {
        return customerRepository.findByUsername(username)
                .map(customer -> customer.getBankAccounts().stream()
                        .map(bankAccount -> new BankAccountOverviewResp(bankAccount.getId(), bankAccount.getIban(),
                                bankAccount.getAccountType(), bankAccount.getCurrency(), bankAccount.getBalance(), bankAccount.getStatus())))
                .map(bankAccountOverviewDTOS -> bankAccountOverviewDTOS.toList())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

    }
}
