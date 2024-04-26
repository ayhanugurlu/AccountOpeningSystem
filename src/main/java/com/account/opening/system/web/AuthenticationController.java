package com.account.opening.system.web;

import com.account.opening.system.service.AuthenticationService;
import com.account.opening.system.service.JwtService;
import com.account.opening.system.service.dto.request.LogonRequest;
import com.account.opening.system.service.dto.request.TokenRequest;
import com.account.opening.system.service.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> token(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok().body(new TokenResponse(tokenRequest.username(), authenticationService.authenticate(tokenRequest)));
    }


    @PostMapping("/logon")
    public void logon(@RequestBody LogonRequest logonRequest) {
        jwtService.validateToken(logonRequest.token());
    }

}
