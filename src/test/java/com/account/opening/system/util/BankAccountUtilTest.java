package com.account.opening.system.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountUtilTest {

    @Test
    void givenCountryCodeBankCodeAndAccountNumber_whenGenerateIBAN_thenReturnIBAN() {
        // Given
        String cc = "NL";
        String bankCode = "ABNA";
        Long accountNumber = 2L;

        // When
        String iban = BankAccountUtil.generateIBAN(cc, bankCode, accountNumber,10);

        // Then
        assertEquals("NL31ABNA0000000002",iban);
    }

    @Test
    void given_WhenGenerateRandomPassword_ThenReturnRandomPassword() {

        // When
        String password = BankAccountUtil.generateRandomPassword();

        // Then
        assertNotNull(password);
        assertEquals(10,password.length());
    }

}
