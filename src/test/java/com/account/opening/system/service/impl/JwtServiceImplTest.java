package com.account.opening.system.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class JwtServiceImplTest {

    JwtServiceImpl jwtService = new JwtServiceImpl();


    @Test
    void givenToken_whenExtractUsername_thenReturnUsername() {
        // Given
        String token = jwtService.generateToken("username");
        // When
        String username = jwtService.extractUsername(token);
        // Then
        assertEquals("username", username);
    }

    @Test
    void givenToken_whenValidateToken_thenReturnTrue() {
        // Given
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        // When
        when(userDetails.getUsername()).thenReturn("username");
        String token = jwtService.generateToken("username");

        boolean valid = jwtService.validateToken(token, userDetails);
        // Then
        assertTrue(valid);
    }

    @Test
    void givenToken_whenExtractExpiration_thenReturnExpirationDate() {
        // Given
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTcxNDExOTY2NywiZXhwIjoxNzE0MTE5NjY3fQ.6ltuhQxN5dxzJrbyLKx5E3VIJ6Ai2Rz2fuCKAp1Mza4";
        // When

        // Then
        ExpiredJwtException expiredJwtException =  Assertions.assertThrows(ExpiredJwtException.class, () -> {
            jwtService.validateToken(expiredToken);
        });
        assertTrue(expiredJwtException.getMessage().contains("JWT expired at"));
    }

}
