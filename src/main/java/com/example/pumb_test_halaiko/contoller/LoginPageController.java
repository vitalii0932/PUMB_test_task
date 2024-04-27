package com.example.pumb_test_halaiko.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginPageController {
    @GetMapping("")
    public String getLoginPage(Model model) {
        return "login";
    }
}
