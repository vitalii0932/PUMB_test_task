package com.example.pumb_test_halaiko.servicesTests;

import com.example.pumb_test_halaiko.dto.AuthenticationRequest;
import com.example.pumb_test_halaiko.dto.RegisterRequest;
import com.example.pumb_test_halaiko.repository.UserRepository;
import com.example.pumb_test_halaiko.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

/**
 * AuthenticationService tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationServiceTests {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    /**
     * register function test
     *
     * @param email - user email
     * @param password - user password
     */
    @Transactional
    @ParameterizedTest
    @CsvSource({
            "admin_test2@com.ua, supersecret!",
            "admin_test@com.ua, supersecret!",
            ","
    })
    public void registerTest(String email, String password){
        // delete new user
        if (email != null && email.equals("admin_test2@com.ua")) {
            deleteTestAdmin(email);
        }

        // set request content
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);

        // send and check the result
        try {
            var registerResponse = authenticationService.register(request);
            assert (!registerResponse.getToken().isEmpty());
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        // delete new user
        if (email != null && email.equals("admin_test2@com.ua")) {
            deleteTestAdmin(email);
        }
    }

    /**
     * delete user from db
     *
     * @param email - user email
     */
    private void deleteTestAdmin(String email) {
        if (userRepository.findUserByEmail(email).isPresent()) {
            userRepository.deleteByEmail(email);
        }
    }

    /**
     * test authenticate function
     *
     * @param email - user email
     * @param password - user password
     */
    @ParameterizedTest
    @CsvSource({
            "admin_test@com.ua, admin",
            "admin_test@com.ua, supersecret!",
            ","
    })
    public void authenticateTest(String email, String password) {
        // set request content
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        // send and check the result
        try {
            var registerResponse = authenticationService.authenticate(request);
            assert (!registerResponse.getToken().isEmpty());
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
