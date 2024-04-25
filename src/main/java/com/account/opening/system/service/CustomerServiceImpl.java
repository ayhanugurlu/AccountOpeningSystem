package com.account.opening.system.service;

import com.account.opening.system.data.BankAccountRepository;
import com.account.opening.system.data.CustomerRepository;
import com.account.opening.system.data.model.Address;
import com.account.opening.system.data.model.BankAccount;
import com.account.opening.system.data.model.Customer;
import com.account.opening.system.exception.IllegalCountryException;
import com.account.opening.system.exception.UsernameAlreadyExists;
import com.account.opening.system.service.dto.CustomerDTO;
import com.account.opening.system.service.dto.UserRegistrationResponseDTO;
import com.account.opening.system.util.BankAccountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;


    BankAccountRepository bAnkAccountRepository;

    @Value("${allowed.countries:NL}")
    String[] allowedCountries;

    @Value("${bank.code:ABNA}")
    String bankCode;


    @Value("${bank.account.length:10}")
    int bankAccountLength = 0;

    @Override
    public UserRegistrationResponseDTO createCustomer(CustomerDTO customerDTO) {

        if(Arrays.stream(allowedCountries).noneMatch(country -> country.equals(customerDTO.addressDTO().country()))){
            throw new IllegalCountryException("Country not allowed");
        }
        customerRepository.findByUsername(customerDTO.username()).ifPresent(customer -> new UsernameAlreadyExists("Username already exists"));


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
        customerRepository.save(customer);
        bankAccount.getId();
        bankAccount.setIban(BankAccountUtil.generateIBAN(customerDTO.addressDTO().country(), bankCode, bankAccount.getId(), bankAccountLength));
        bAnkAccountRepository.save(bankAccount);
        return new UserRegistrationResponseDTO(customer.getUsername(), customer.getPassword());
    }

    @Override
    public CustomerDTO getCustomer(String iban) {
        return null;
    }


}
