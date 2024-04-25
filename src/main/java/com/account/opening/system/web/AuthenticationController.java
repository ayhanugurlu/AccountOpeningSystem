package com.account.opening.system.web;

import com.account.opening.system.service.AuthenticationService;
import com.account.opening.system.service.dto.request.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/token")
    public String token(@RequestBody TokenRequest tokenRequest) {
        return authenticationService.authenticate(tokenRequest);
    }

    @GetMapping("/test")
    public String token() {
        return "Hello";
    }

}
