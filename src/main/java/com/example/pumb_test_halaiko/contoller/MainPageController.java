package com.example.pumb_test_halaiko.contoller;

import com.example.pumb_test_halaiko.enums.Sex;
import com.example.pumb_test_halaiko.model.Animal;
import com.example.pumb_test_halaiko.service.CategoryService;
import com.example.pumb_test_halaiko.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * controller for loading the home page
 */
@Controller
@RequestMapping("/api/v1/test_task")
@RequiredArgsConstructor
public class MainPageController {

    private final CategoryService categoryService;
    private final TypeService typeService;

    /**
     * load main page function
     *
     * @param model - the model object used to pass data to the view
     * @return the view for the main page
     */
    @GetMapping("")
    public String getMainPage(Model model) {
        var typesFields = typeService.getAllNames();
        typesFields.addAll(categoryService.getAllNames());

        typesFields.addAll(Arrays.stream(Sex.values())
                .map(sex -> "sex." + sex.toString().toLowerCase())
                .toList());

        model.addAttribute("typesFields", typesFields);

        model.addAttribute("animalFields", Animal.getAnimalFields());
        return "index";
    }
}
