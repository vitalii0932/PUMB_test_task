package com.example.pumb_test_halaiko.contollersTests;

import com.example.pumb_test_halaiko.dto.AuthenticationRequest;
import com.example.pumb_test_halaiko.dto.RegisterRequest;
import com.example.pumb_test_halaiko.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AuthController functions test
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    /**
     * test login page loading
     *
     * @throws Exception if something wrong
     */
    @Test
    public void getLoginPageTest_shouldReturnViewWithSameTitle() throws Exception {
        this.mockMvc.perform(
                        get("/api/v1/auth"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Login page</title>")));
    }

    /**
     * test registration function
     *
     * @param email - user email
     * @param password - user password
     * @param expectedResult - registration expected result
     * @throws Exception if something wrong
     */
    @Transactional
    @ParameterizedTest
    @CsvSource({
            "admin_test2@com.ua, supersecret!, true",
            "admin_test@com.ua, supersecret!, false",
            ",,false"
    })
    public void registerTest(String email, String password, Boolean expectedResult) throws Exception {
        // delete new user
        if (email != null && email.equals("admin_test2@com.ua")) {
            deleteTestAdmin(email);
        }

        // set request content
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);

        // send and check the request
        checkAuthRegResponse("/api/v1/auth/registration", request, expectedResult);

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
     * @param expectedResult - authentication expected result
     * @throws Exception if something wrong
     */
    @ParameterizedTest
    @CsvSource({
            "admin_test@com.ua, admin, true",
            "admin_test@com.ua, supersecret!, false",
            ",,false"
    })
    public void authenticateTest(String email, String password, Boolean expectedResult) throws Exception {
        // set request content
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        // send and check the request
        checkAuthRegResponse("/api/v1/auth/authenticate", request, expectedResult);
    }

    /**
     * check the authorization and registration results
     *
     * @param url - request url
     * @param userData - request content
     * @param expectedResult - expected response result
     * @throws Exception if something wrong
     */
    private void checkAuthRegResponse(String url, Object userData, Boolean expectedResult) throws Exception {
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(expectedResult ? status().isOk() : status().isForbidden())
                .andExpect(expectedResult ? content().string(containsString("token")) : content().string(""));
    }
}
