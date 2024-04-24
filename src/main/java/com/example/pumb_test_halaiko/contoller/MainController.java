package com.example.pumb_test_halaiko.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/test_task")
@RequiredArgsConstructor
public class MainController {

    @GetMapping("")
    public String getMainPage(Model model) {
        return "index";
    }
}
