package com.account.opening.system.service.impl;

import com.account.opening.system.exception.UsernameNotFoundException;
import com.account.opening.system.service.AuthenticationService;
import com.account.opening.system.service.JwtService;
import com.account.opening.system.service.dto.request.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;


    private final JwtService jwtService;

    @Override
    public String authenticate(TokenRequest tokenRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(tokenRequest.username(), tokenRequest.password()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(tokenRequest.username());

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }
}
