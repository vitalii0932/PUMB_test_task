package com.example.pumb_test_halaiko.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * controller for loading the login page
 */
@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginPageController {

    /**
     * load login page function
     *
     * @param model - the model object used to pass data to the view
     * @return the view for the login page
     */
    @GetMapping("")
    public String getLoginPage(Model model) {
        return "login";
    }
}
