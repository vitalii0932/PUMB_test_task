package com.example.pumb_test_halaiko;

import com.example.pumb_test_halaiko.dto.AuthenticationRequest;
import com.example.pumb_test_halaiko.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @ParameterizedTest
    @CsvSource({
            "/api/v1/test_task, <title>Main page</title>, true",
            "/api/v1/auth, <title>Login page</title>, true",
            "/h2-console,, false"
    })
    public void urlSecurityTest(String url, String expectedPage, Boolean expectedResult) throws Exception {
        this.mockMvc.perform(
                get(url))
                .andDo(print())
                .andExpect(expectedResult ? status().isOk() : status().is3xxRedirection())
                .andExpect(expectedResult ? content().string(containsString(expectedPage)) : content().string(""));
    }
}
