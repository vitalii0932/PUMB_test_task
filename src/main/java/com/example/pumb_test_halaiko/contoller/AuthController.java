package com.example.pumb_test_halaiko.contoller;

import com.example.pumb_test_halaiko.dto.AuthenticationRequest;
import com.example.pumb_test_halaiko.dto.AuthenticationResponse;
import com.example.pumb_test_halaiko.dto.RegisterRequest;
import com.example.pumb_test_halaiko.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication pages
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller", description = "Контроллер для реєстрації нових користувачів та входу в систему вже існуючих для доступу до h2-console")
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * handles the POST request for user registration
     *
     * @param request - the RegisterRequest object containing user registration data
     * @return ResponseEntity with an AuthenticationResponse containing registration status
     */
    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    /**
     * handles the POST request for user authentication
     *
     * @param request - the AuthenticationRequest object containing user authentication data
     * @return ResponseEntity with an AuthenticationResponse containing authentication status
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
