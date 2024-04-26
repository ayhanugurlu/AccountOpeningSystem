package com.account.opening.system.service.impl;

import com.account.opening.system.service.AuthenticationService;
import com.account.opening.system.service.JwtService;
import com.account.opening.system.service.dto.request.TokenRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceImplTest {

    @Test
    void givenTokenRequest_whenAuthenticate_thenReturnToken() {
        // Given
        TokenRequest tokenRequest = new TokenRequest("username", "password");
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(tokenRequest.username())).thenReturn("token");
        AuthenticationService authenticationService = new AuthenticationServiceImpl(authenticationManager, jwtService);

        // When
        String result = authenticationService.authenticate(tokenRequest);

        // Then
        assertEquals("token", result);
    }

    @Test
    void givenInvalidTokenRequest_whenAuthenticate_thenThrowAuthenticationException() {
        // Given
        TokenRequest tokenRequest = new TokenRequest("username", "password");
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(tokenRequest.username())).thenReturn("token");
        AuthenticationService authenticationService = new AuthenticationServiceImpl(authenticationManager, jwtService);

        // When
        Exception exception = assertThrows(Exception.class, () -> authenticationService.authenticate(tokenRequest));

        // Then
        assertEquals("invalid user request!!", exception.getMessage());
    }

}
