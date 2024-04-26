package com.account.opening.system.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * JwtService interface
 **/
public interface JwtService {
    /**
     * Generate token
     *
     * @param username
     * @return
     */
    String generateToken(String username);

    /**
     * @param token
     * @return
     */
    String extractUsername(String token);


    /**
     * Validate token
     *
     * @param token
     * @param userDetails
     * @return
     */
    boolean validateToken(String token, UserDetails userDetails);


    /**
     * Validate token
     *
     * @param token
     * @return
     */
    boolean validateToken(String token);
}
