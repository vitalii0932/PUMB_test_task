package com.example.pumb_test_halaiko.contoller;

import com.example.pumb_test_halaiko.dto.AuthenticationRequest;
import com.example.pumb_test_halaiko.dto.AuthenticationResponse;
import com.example.pumb_test_halaiko.dto.RegisterRequest;
import com.example.pumb_test_halaiko.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller for auth functions
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
    @Operation(summary = "Register new user in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success of user registration."),
            @ApiResponse(responseCode = "403", description = "Registry error. A user with this email address has already been registered.")
    })
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
    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success of user authentication."),
            @ApiResponse(responseCode = "403", description = "Authentication error. Incorrect data was entered.")
    })
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
