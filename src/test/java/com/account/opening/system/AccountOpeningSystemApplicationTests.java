package com.account.opening.system;

import com.account.opening.system.service.dto.AddressDTO;
import com.account.opening.system.service.dto.request.CustomerRegistrationReq;
import com.account.opening.system.service.dto.request.LogonRequest;
import com.account.opening.system.service.dto.request.TokenRequest;
import com.account.opening.system.service.dto.response.BankAccountOverviewResp;
import com.account.opening.system.service.dto.response.CustomerRegistrationRes;
import com.account.opening.system.service.dto.response.TokenResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/test.properties")

class AccountOpeningSystemApplicationTests {

    @LocalServerPort
    int localPort;

    TestRestTemplate testRestTemplate = new TestRestTemplate();


    @BeforeAll
    static void setup() {
        System.setProperty("spring.profiles.active", "test");
    }

    @Test
    void givenCustomerCreateRequest_whenCreateCustomer_thenStatusCreated() {
        // given
        String url = String.format("http://localhost:%d/register", localPort);
        AddressDTO addressDTO = new AddressDTO("street", "city", "zip", "country", "NL");
        Date date = Date.from(LocalDateTime.parse("2000-12-30T19:34:50.63").toInstant(java.time.ZoneOffset.UTC));
        CustomerRegistrationReq request = new CustomerRegistrationReq("name", "username", date, addressDTO, "docId", "EUR", "ACTIVE", "SAVINGS", 100.0);


        // when
        ResponseEntity<CustomerRegistrationRes> response = testRestTemplate.postForEntity(url, request, CustomerRegistrationRes.class);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("username", response.getBody().username());
        assertNotNull(response.getBody().password());
    }


    @Test
    void givenCustomerCreateRequestNotAllowedCountyCode_whenCreateCustomer_thenStatusUnprocessableEntity() {
        // given
        String url = String.format("http://localhost:%d/register", localPort);
        AddressDTO addressDTO = new AddressDTO("street", "city", "zip", "country", "NL123");
        Date date = Date.from(LocalDateTime.parse("2000-12-30T19:34:50.63").toInstant(java.time.ZoneOffset.UTC));
        CustomerRegistrationReq request = new CustomerRegistrationReq("name", "username123", date, addressDTO, "docId", "EUR", "ACTIVE", "SAVINGS", 100.0);


        // when
        ResponseEntity<?> response = testRestTemplate.postForEntity(url, request, String.class);

        // then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Country not allowed", response.getBody());
    }


    @Test
    void givenCustomerCreateRequestUserAgeLessThen18_whenCreateCustomer_thenStatusUnprocessableEntity() {
        // given
        String url = String.format("http://localhost:%d/register", localPort);
        AddressDTO addressDTO = new AddressDTO("street", "city", "zip", "country", "NL123");
        Date date = Date.from(LocalDateTime.parse("2023-12-30T19:34:50.63").toInstant(java.time.ZoneOffset.UTC));
        CustomerRegistrationReq request = new CustomerRegistrationReq("name", "username123", date, addressDTO, "docId", "EUR", "ACTIVE", "SAVINGS", 100.0);


        // when
        ResponseEntity<?> response = testRestTemplate.postForEntity(url, request, String.class);

        // then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Country not allowed", response.getBody());
    }

    @Test
    void givenUsernameAlreadyExists_whenCreateCustomer_thenStatusUnprocessableEntity() {
        // given
        String url = String.format("http://localhost:%d/register", localPort);
        AddressDTO addressDTO = new AddressDTO("street", "city", "zip", "country", "NL123");
        Date date = Date.from(LocalDateTime.parse("2023-12-30T19:34:50.63").toInstant(java.time.ZoneOffset.UTC));
        CustomerRegistrationReq request = new CustomerRegistrationReq("name", "username123", date, addressDTO, "docId", "EUR", "ACTIVE", "SAVINGS", 100.0);


        // when
        ResponseEntity<?> response = testRestTemplate.postForEntity(url, request, String.class);

        // then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Country not allowed", response.getBody());
    }


    @Test
    void givenUsername_whenGetCustomerOverview_thenStatusOk() {
        // given
        String urlRegister = String.format("http://localhost:%d/register", localPort);
        AddressDTO addressDTO = new AddressDTO("street", "city", "zip", "country", "NL");
        Date date = Date.from(LocalDateTime.parse("2000-12-30T19:34:50.63").toInstant(java.time.ZoneOffset.UTC));
        CustomerRegistrationReq request = new CustomerRegistrationReq("name", "usernameOverview", date, addressDTO, "docId", "EUR", "ACTIVE", "SAVINGS", 100.0);
        String urlOverview = String.format("http://localhost:%d/overview/usernameOverview", localPort);
        String urlLogin = String.format("http://localhost:%d/token", localPort);

        // when
        ResponseEntity<CustomerRegistrationRes> responseRegister = testRestTemplate.postForEntity(urlRegister, request, CustomerRegistrationRes.class);
        ResponseEntity<TokenResponse> tokenResponseResponseEntity = testRestTemplate.postForEntity(urlLogin, new TokenRequest(responseRegister.getBody().username(), responseRegister.getBody().password()), TokenResponse.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + tokenResponseResponseEntity.getBody().token());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<BankAccountOverviewResp[]> response = testRestTemplate.exchange(urlOverview, HttpMethod.GET, entity, BankAccountOverviewResp[].class);


        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SAVINGS", response.getBody()[0].accountType());
        assertEquals("EUR", response.getBody()[0].currency());
        assertEquals(100.0, response.getBody()[0].balance());
    }


    @Test
    void givenTokenRequest_whenToken_thenStatusOk() {

        // given
        String urlRegister = String.format("http://localhost:%d/register", localPort);
        AddressDTO addressDTO = new AddressDTO("street", "city", "zip", "country", "NL");
        Date date = Date.from(LocalDateTime.parse("2000-12-30T19:34:50.63").toInstant(java.time.ZoneOffset.UTC));
        CustomerRegistrationReq request = new CustomerRegistrationReq("name", "usernameTokenRequest", date, addressDTO, "docId", "EUR", "ACTIVE", "SAVINGS", 100.0);
        String urlLogin = String.format("http://localhost:%d/token", localPort);

        TokenRequest tokenRequest = new TokenRequest("username", "password");

        // when
        ResponseEntity<CustomerRegistrationRes> responseRegister = testRestTemplate.postForEntity(urlRegister, request, CustomerRegistrationRes.class);
        ResponseEntity<TokenResponse> response = testRestTemplate.postForEntity(urlLogin, new TokenRequest(responseRegister.getBody().username(), responseRegister.getBody().password()), TokenResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("usernameTokenRequest", response.getBody().username());
        assertNotNull(response.getBody().token());

    }


    @Test
    void givenTokenRequestUserNotExist_whenToken_thenStatusOk() {
        // given
        String urlLogin = String.format("http://localhost:%d/token", localPort);
        TokenRequest tokenRequest = new TokenRequest("usernameusernameusername", "password");
        // when
        ResponseEntity<TokenResponse> response = testRestTemplate.postForEntity(urlLogin, tokenRequest, TokenResponse.class);

        // then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void givenToken_WhenLogon_thenStatusOk() {
        // given
        String urlRegister = String.format("http://localhost:%d/register", localPort);
        AddressDTO addressDTO = new AddressDTO("street", "city", "zip", "country", "NL");
        Date date = Date.from(LocalDateTime.parse("2000-12-30T19:34:50.63").toInstant(java.time.ZoneOffset.UTC));
        CustomerRegistrationReq request = new CustomerRegistrationReq("name", "usernameLogon", date, addressDTO, "docId", "EUR", "ACTIVE", "SAVINGS", 100.0);
        String urlLogin = String.format("http://localhost:%d/token", localPort);

        // when
        ResponseEntity<CustomerRegistrationRes> responseRegister = testRestTemplate.postForEntity(urlRegister, request, CustomerRegistrationRes.class);
        ResponseEntity<TokenResponse> tokenResponseResponseEntity = testRestTemplate.postForEntity(urlLogin, new TokenRequest(responseRegister.getBody().username(), responseRegister.getBody().password()), TokenResponse.class);

        // then
        assertEquals(HttpStatus.OK, tokenResponseResponseEntity.getStatusCode());
    }


    @Test
    void givenTokenInvalid_WhenLogon_thenStatusForbidden() {
        // given
        String urlLogin = String.format("http://localhost:%d/logon", localPort);

        // when
        LogonRequest logonRequest = new LogonRequest("token");
        ResponseEntity<?> response = testRestTemplate.postForEntity(urlLogin, logonRequest, String.class);

        // then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }
}
