package com.account.opening.system.service.impl;

import com.account.opening.system.data.BankAccountRepository;
import com.account.opening.system.data.CustomerRepository;
import com.account.opening.system.data.model.Address;
import com.account.opening.system.data.model.BankAccount;
import com.account.opening.system.data.model.Customer;
import com.account.opening.system.exception.BankAccountNotFoundException;
import com.account.opening.system.exception.CustomerAgeLimitException;
import com.account.opening.system.exception.IllegalCountryException;
import com.account.opening.system.exception.UsernameAlreadyExists;
import com.account.opening.system.service.CustomerService;
import com.account.opening.system.service.dto.request.CustomerRegistrationReq;
import com.account.opening.system.service.dto.response.BankAccountOverviewResp;
import com.account.opening.system.service.dto.response.CustomerRegistrationRes;
import com.account.opening.system.util.BankAccountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final BankAccountRepository bAnkAccountRepository;

    @Value("${allowed.countries:NL,BEL}")
    String[] allowedCountries;

    @Value("${bank.code:ABNA}")
    String bankCode;


    @Value("${bank.account.length:10}")
    int bankAccountLength = 0;

    private static final int AGE_LIMIT = 18;


    private static final String COUNTRY_CODE = "NL";


    @Override
    public CustomerRegistrationRes createCustomer(CustomerRegistrationReq customerDTO) {

        if (Arrays.stream(allowedCountries).noneMatch(country -> country.equals(customerDTO.addressDTO().country()))) {
            throw new IllegalCountryException("Country not allowed");
        }
        if (customerRepository.findByUsername(customerDTO.username()).isPresent()) {
            throw new UsernameAlreadyExists("Username already exists");
        }


        if (customerDTO.dateOfBirth().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().plusYears(AGE_LIMIT).isAfter(LocalDateTime.now())) {
            throw new CustomerAgeLimitException("Customer must be at least 18 years old");
        }
        Address address = Address.builder().country(customerDTO.addressDTO()
                        .country()).city(customerDTO.addressDTO().city())
                .build();
        BankAccount bankAccount = BankAccount.builder()
                .currency(customerDTO.currency())
                .accountType(customerDTO.accountType())
                .balance(customerDTO.balance())
                .status(customerDTO.status())
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
        bankAccount.setIban(BankAccountUtil.generateIBAN(COUNTRY_CODE, bankCode, bankAccount.getId(), bankAccountLength));
        bAnkAccountRepository.save(bankAccount);
        return new CustomerRegistrationRes(customer.getUsername(), customer.getPassword());
    }

    @Override
    public List<BankAccountOverviewResp> getCustomerOverview(String username) {
        return customerRepository.findByUsername(username).map(Customer::getBankAccounts).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"))
                .stream().map(bankAccount -> new BankAccountOverviewResp(bankAccount.getId(), bankAccount.getIban(), bankAccount.getAccountType(), bankAccount.getCurrency(), bankAccount.getBalance(), bankAccount.getStatus())).toList();


    }
}
