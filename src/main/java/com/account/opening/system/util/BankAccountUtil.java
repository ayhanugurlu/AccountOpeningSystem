package com.account.opening.system.util;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.util.Optional;
import java.util.Random;

public class BankAccountUtil {

    private static final Random random = new Random();

    private BankAccountUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateIBAN(String cc, String bankCode, Long accountNumber, int bankAccountLength) {
        CountryCode countryCode = Optional.ofNullable(CountryCode.getByCode(cc)).orElseThrow(() -> new IllegalArgumentException("Invalid country code"));

        Iban iban = new Iban.Builder()
                .countryCode(countryCode)
                .bankCode(bankCode)
                .accountNumber(prepareAccountNumber(accountNumber, bankAccountLength))
                .build();
        return iban.toString();
    }

    public static String prepareAccountNumber(Long accountNumber, int bankAccountLength) {
        String accountNumberStr = accountNumber.toString();
        int accountNumberLength = accountNumberStr.length();
        if (accountNumberLength > bankAccountLength) {
            throw new IllegalArgumentException("Account number is too long");
        }
        return "0".repeat(bankAccountLength - accountNumberLength) + accountNumberStr;
    }

    public static String generateRandomPassword() {
        return random.ints(10, 33, 122).collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }



}
