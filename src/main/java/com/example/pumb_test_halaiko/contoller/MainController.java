package com.example.pumb_test_halaiko.contoller;

import com.example.pumb_test_halaiko.service.AnimalService;
import com.example.pumb_test_halaiko.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * controller for functions in main page
 */
@RestController
@RequestMapping("/api/v1/test_task")
@RequiredArgsConstructor
@Tag(name = "Main controller", description = "Головний контроллер з можливостями завантаження файлів та пошуку елементів у БД")
public class MainController {

    private final AnimalService animalService;
    private final FileService fileService;

    /**
     * upload file from user
     *
     * @param multipartFile - file
     * @return download file result response
     */
    @Operation(summary = "Upload a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully."),
            @ApiResponse(responseCode = "403", description = "Error in file processing. Perhaps the file is empty or has incorrect values.")
    })
    @PostMapping(value = "/files/uploads", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            fileService.readFile(multipartFile);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }

        return ResponseEntity.ok("File was upload successfully");
    }

    /**
     * receive filtered and sorted data.
     *
     * @param filter   - filtering parameter
     * @param filterBy - additional filtering parameter
     * @param sort     - sorting parameter
     * @param sortBy   - additional sorting parameter
     * @return response with data
     */
    @Operation(summary = "Getting filtered and sorted data from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful data retrieval."),
            @ApiResponse(responseCode = "403", description = "Error while processing a request. Incorrect data entered.")
    })
    @GetMapping("/filter")
    public ResponseEntity<?> filterData(
            @RequestParam(name = "filter") String filter,
            @RequestParam(name = "filterBy") String filterBy,
            @RequestParam(name = "sort") String sort,
            @RequestParam(name = "sortBy") String sortBy
    ) {
        try {
            return ResponseEntity.ok(animalService.findAnimalsByParams(filter, filterBy, sort, sortBy));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }
}
