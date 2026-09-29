package com.winwin.authapi.controller;

import com.winwin.authapi.dto.AuthRequest;
import com.winwin.authapi.dto.ProcessRequest;
import com.winwin.authapi.dto.TokenResponse;
import com.winwin.authapi.entity.User;
import com.winwin.authapi.service.AuthService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AuthRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(new TokenResponse(authService.login(request)));
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> process(@Valid @RequestBody ProcessRequest request,
                                                      @AuthenticationPrincipal User user) {
        String result = authService.processText(user, request.text());
        return ResponseEntity.ok(Map.of("result", result));
    }
}
