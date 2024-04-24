package com.example.pumb_test_halaiko.contoller;

import com.example.pumb_test_halaiko.enums.Filter;
import com.example.pumb_test_halaiko.pojo.Animal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/v1/test_task")
@RequiredArgsConstructor
public class MainController {

    @GetMapping
    public String getMainPage(Model model) {
        return "index";
    }

    @PostMapping("/files/uploads")
    public boolean uploadFile(MultipartFile multipartFile) {
        return false;
    }

    @GetMapping
    public ResponseEntity<List<Animal>> filterData(
            @RequestParam(name = "filter") Filter filter,
            @RequestParam(name = "sort") String sort
    ) {
        return ResponseEntity.ok(null);
    }

}
