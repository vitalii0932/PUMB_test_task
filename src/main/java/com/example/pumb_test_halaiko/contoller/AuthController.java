package com.example.pumb_test_halaiko.contoller;

import com.example.pumb_test_halaiko.dto.AuthenticationRequest;
import com.example.pumb_test_halaiko.dto.AuthenticationResponse;
import com.example.pumb_test_halaiko.dto.RegisterRequest;
import com.example.pumb_test_halaiko.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for authentication pages
 */
@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @GetMapping("")
    public String loginPage(Model model) {
        return "login";
    }

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
        return ResponseEntity.ok(authenticationService.register(request));
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
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
