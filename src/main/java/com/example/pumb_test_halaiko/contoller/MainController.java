package com.example.pumb_test_halaiko.contoller;

import com.example.pumb_test_halaiko.enums.Sex;
import com.example.pumb_test_halaiko.model.Animal;
import com.example.pumb_test_halaiko.model.Type;
import com.example.pumb_test_halaiko.repository.TypeRepository;
import com.example.pumb_test_halaiko.service.AnimalService;
import com.example.pumb_test_halaiko.service.CategoryService;
import com.example.pumb_test_halaiko.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * controller for main page
 */
@Controller
@RequestMapping("/api/v1/test_task")
@RequiredArgsConstructor
public class MainController {

    private final AnimalService animalService;
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

        for(var sex : Sex.values()) {
            typesFields.add("sex." + sex.toString().toLowerCase());
        }

        model.addAttribute("typesFields", typesFields);

        model.addAttribute("animalFields", Animal.getAnimalFields());
        return "index";
    }

    /**
     * upload file from user
     *
     * @param multipartFile - file
     * @return download file result response
     */
    @PostMapping("/files/uploads")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                             RedirectAttributes redirectAttributes) {
        try {
            animalService.readFile(multipartFile);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }

        return ResponseEntity.ok("File was upload successfully");
    }

    /**
     * receive filtered and sorted data.
     *
     * @param filter - filtering parameter
     * @param filterBy - additional filtering parameter
     * @param sort - sorting parameter
     * @param sortBy - additional sorting parameter
     * @return response with data
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Animal>> filterData(
            @RequestParam(name = "filter") String filter,
            @RequestParam(name = "filterBy") String filterBy,
            @RequestParam(name = "sort") String sort,
            @RequestParam(name = "sortBy") String sortBy
    ) {
        return ResponseEntity.ok(animalService.findAnimalsByParams(filter, filterBy, sort, sortBy));
    }

}
