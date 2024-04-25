package com.account.opening.system.service;

import com.account.opening.system.data.BankAccountRepository;
import com.account.opening.system.data.CustomerRepository;
import com.account.opening.system.data.model.Address;
import com.account.opening.system.data.model.BankAccount;
import com.account.opening.system.data.model.Customer;
import com.account.opening.system.exception.IllegalCountryException;
import com.account.opening.system.exception.UsernameAlreadyExists;
import com.account.opening.system.service.dto.AddressDTO;
import com.account.opening.system.service.dto.request.CustomerCreateReq;
import com.account.opening.system.service.dto.response.UserRegistrationRes;
import com.account.opening.system.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    BankAccountRepository bankAccountRepository = Mockito.mock(BankAccountRepository.class);
    private CustomerServiceImpl customerService = new CustomerServiceImpl(customerRepository, bankAccountRepository);


    @Test
    void givenCustomerDTO_whenCreateCustomer_thenUserRegistrationResponseDTO() {
        // Given
        LocalDate date = LocalDate.parse("2018-05-05");
        AddressDTO addressDTO = new AddressDTO("6846", "Amsterdam", "Amsterdam", "Arnhem", "NL");
        CustomerCreateReq customerDTO = new CustomerCreateReq("John", "john.doe", new Date(date.toEpochDay()), addressDTO, "AB123");
        ReflectionTestUtils.setField(customerService, "allowedCountries", new String[]{"NL"});
        ReflectionTestUtils.setField(customerService, "bankCode", "ABNA");
        ReflectionTestUtils.setField(customerService, "bankAccountLength", 10);

        // When
        Address address = Address.builder()
                .id(1L)
                .country("NL")
                .city("Amsterdam")
                .addressLine2("Arnhem 123")
                .build();
        BankAccount bankAccount = BankAccount.builder()
                .id(1L)
                .build();
        Customer customer = Customer.builder()
                .id(1L)
                .name("John")
                .password("password")
                .username("john.doe")
                .address(address)
                .bankAccounts(Arrays.asList(bankAccount))
                .build();
        when(customerRepository.findByUsername("john.doe")).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(customer);
        when(bankAccountRepository.save(any())).thenReturn(BankAccount.builder()
                .id(1L)
                .iban("NL91ABNA0417164300")
                .build());
        UserRegistrationRes userRegistrationRes = customerService.createCustomer(customerDTO);


        // Then
        assertNotNull(userRegistrationRes);
        assertEquals("john.doe", userRegistrationRes.username());
        assertNotNull(userRegistrationRes.password());
    }

    @Test
    void givenCustomerDTOWithNotAllowedCompanyCode_whenCreateCustomer_thenIllegalCountryException() {
        // Given
        LocalDate date = LocalDate.parse("2018-05-05");
        AddressDTO addressDTO = new AddressDTO("6846", "Amsterdam", "Amsterdam", "Arnhem", "AB");
        CustomerCreateReq customerDTO = new CustomerCreateReq("John", "john.doe", new Date(date.toEpochDay()), addressDTO, "AB123");
        ReflectionTestUtils.setField(customerService, "allowedCountries", new String[]{"NL","BE"});
        ReflectionTestUtils.setField(customerService, "bankCode", "ABNA");
        ReflectionTestUtils.setField(customerService, "bankAccountLength", 10);

        // When
        IllegalCountryException illegalCountryException = Assertions.assertThrows(IllegalCountryException.class, () -> {
            customerService.createCustomer(customerDTO);
        });
        // Then
        assertEquals("Country not allowed", illegalCountryException.getMessage());
    }

    @Test
    void givenCustomerDTOWithNotAllowedCompanyCode_whenCreateCustomer_thenUsernameAlreadyExists() {
        // Given
        LocalDate date = LocalDate.parse("2018-05-05");
        AddressDTO addressDTO = new AddressDTO("6846", "Amsterdam", "Amsterdam", "Arnhem", "NL");
        CustomerCreateReq customerDTO = new CustomerCreateReq("John", "john.doe", new Date(date.toEpochDay()), addressDTO, "AB123");
        ReflectionTestUtils.setField(customerService, "allowedCountries", new String[]{"NL","BE"});
        ReflectionTestUtils.setField(customerService, "bankCode", "ABNA");
        ReflectionTestUtils.setField(customerService, "bankAccountLength", 10);

        // When
        Address address = Address.builder()
                .id(1L)
                .country("NL")
                .city("Amsterdam")
                .addressLine2("Arnhem 123")
                .build();
        BankAccount bankAccount = BankAccount.builder()
                .id(1L)
                .build();
        Customer customer = Customer.builder()
                .id(1L)
                .name("John")
                .password("password")
                .username("john.doe")
                .address(address)
                .bankAccounts(Arrays.asList(bankAccount))
                .build();
        when(customerRepository.findByUsername("john.doe")).thenReturn(Optional.of(customer));
        UsernameAlreadyExists usernameAlreadyExists = Assertions.assertThrows(UsernameAlreadyExists.class, () -> {
            customerService.createCustomer(customerDTO);
        });
        // Then
        assertEquals("Username already exists", usernameAlreadyExists.getMessage());
    }

}
