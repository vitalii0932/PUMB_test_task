package com.example.pumb_test_halaiko.service;

import com.example.pumb_test_halaiko.config.JwtService;

import com.example.pumb_test_halaiko.dto.AuthenticationRequest;
import com.example.pumb_test_halaiko.dto.AuthenticationResponse;
import com.example.pumb_test_halaiko.dto.RegisterRequest;
import com.example.pumb_test_halaiko.model.User;
import com.example.pumb_test_halaiko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * service class for Authentication
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * user registration in the system and sending a jwt token
     *
     * @param request - request from user with register-parameters
     * @return jwt token
     */
    public AuthenticationResponse register(RegisterRequest request) throws RuntimeException {
        if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("This email is taken already");
        }
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        var jwt = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    /**
     * user authentication in the system and sending a jwt token
     *
     * @param request - request from user with auth-parameters
     * @return jwt token
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws RuntimeException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}