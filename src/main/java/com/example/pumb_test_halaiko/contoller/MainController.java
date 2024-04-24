package com.example.pumb_test_halaiko.contoller;

import com.example.pumb_test_halaiko.pojo.Animal;
import com.example.pumb_test_halaiko.pojo.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * controller for main page
 */
@Controller
@RequestMapping("/api/v1/test_task")
@RequiredArgsConstructor
public class MainController {

    /**
     * load main page function
     *
     * @param model - the model object used to pass data to the view
     * @return the view for the main page
     */
    @GetMapping("")
    public String getMainPage(Model model) {
        return "index";
    }

    /**
     * upload file from user
     *
     * @param multipartFile - file
     * @return download file result response
     */
    @PostMapping("/files/uploads")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        System.out.println(multipartFile.getSize());
        return ResponseEntity.ok(String.valueOf(multipartFile.getSize()));
    }

    /**
     * receive filtered and sorted data.
     *
     * @param filter - filtering parameter
     * @param sort - sorting parameter
     * @param sortLike - additional sorting parameter
     * @return response with data
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Animal>> filterData(
            @RequestParam(name = "filter") String filter,
            @RequestParam(name = "sort") String sort,
            @RequestParam(name = "sortLike") String sortLike
    ) {
        return ResponseEntity.ok(null);
    }

}
