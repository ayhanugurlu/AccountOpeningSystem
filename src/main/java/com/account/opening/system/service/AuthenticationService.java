package com.account.opening.system.service;

import com.account.opening.system.service.dto.request.TokenRequest;

public interface AuthenticationService {
    String authenticate(TokenRequest tokenRequest);
}
