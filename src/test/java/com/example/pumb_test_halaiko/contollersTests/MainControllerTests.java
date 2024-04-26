package com.example.pumb_test_halaiko.contollersTests;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.pumb_test_halaiko.model.Animal;
import com.example.pumb_test_halaiko.service.AnimalService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * MainController functions test
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * test main page loading
     *
     * @throws Exception if something wrong
     */
    @Test
    public void getMainPageTest_shouldReturnViewWithSameTitle() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/test_task"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Main page</title>")));
    }

    /**
     * test uploading different files
     *
     * @param fileName - file name
     * @param contentType - file content type
     * @param expectSuccess - expected result
     * @throws Exception if something wrong
     */
    @ParameterizedTest
    @CsvSource({
            "animals.csv, text/csv, true",
            "animals.xml, text/xml, true",
            "test.txt, text/plain, false"
    })
    public void uploadFileTest(String fileName, String contentType, boolean expectSuccess) throws Exception {
        byte[] fileBytes = readFileFromResources(fileName);
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, contentType, fileBytes);

        if (expectSuccess) {
            mockMvc.perform(multipart("/api/v1/test_task/files/uploads").file(multipartFile))
                    .andExpect(status().isOk());
        } else {
            mockMvc.perform(multipart("/api/v1/test_task/files/uploads").file(multipartFile))
                    .andExpect(status().isForbidden());
        }
    }

    /**
     * reading data from file by its name
     *
     * @param fileName - file name
     * @return a byte array read from file
     * @throws IOException if something wrong
     */
    private byte[] readFileFromResources(String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:" + fileName);

        FileInputStream input = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        input.read(bytes);
        input.close();

        return bytes;
    }

    /**
     * check filter data function with success result
     *
     * @param filter - filter param
     * @param filterBy - filter value
     * @param sort - sort param
     * @param sortBy - sort type
     * @throws Exception if something wrong
     */
    @ParameterizedTest
    @CsvSource({
            "type,dog,id,desc",
            "category,First category,id,desc",
            "sex,female,id,desc"
    })
    public void FilterDataTest_Success(String filter, String filterBy, String sort, String sortBy) throws Exception {
        checkFilteredData(filter, filterBy, sort, sortBy);
    }

    /**
     * check filtered data function
     *
     * @param filter - filter param
     * @param filterBy - filter value
     * @param sort - sort param
     * @param sortBy - sort type
     * @throws Exception if something wrong
     */
    private void checkFilteredData(String filter, String filterBy, String sort, String sortBy) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/test_task/filter")
                        .param("filter", filter)
                        .param("filterBy", filterBy)
                        .param("sort", sort)
                        .param("sortBy", sortBy)) // perform a GET request with parameters
                .andExpect(status().isOk()) // expecting HTTP status 200 (OK)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // expecting JSON content type
                .andReturn(); // get the result of the performed action

        String jsonResponse = mvcResult.getResponse().getContentAsString(); // extract JSON response
        List<Animal> responseList = new ObjectMapper().readValue(jsonResponse, new TypeReference<List<Animal>>() {});

        int lastElemId = -1; // set n-1 element id
        for (var elem : responseList) {
            /* filtering check */
            switch (filter) {
                // checks if you're taking animals with a certain type
                case "type" -> {
                    assert (elem.getType().getName().equals(filterBy));
                }
                // checks if you're taking animals with a certain category
                case "category" -> {
                    assert (elem.getCategory().getName().equals(filterBy));
                }
                // checks if you're taking animals with a certain sex
                case "sex" -> {
                    assert (elem.getSex().equals(filterBy));
                }
            }

            /* sorting check */
            if (lastElemId == -1) {
                // set the identifier of element n-1, if it is the first iter
                lastElemId = elem.getId();
            } else {
                // if this isn't the first iter, check the elements sorting
                switch (sortBy) {
                    // desc sorting check
                    case "desc" -> {
                        assert (lastElemId >= elem.getId());
                    }
                    // asc sorting check
                    case "asc" -> {
                        assert (lastElemId <= elem.getId());
                    }
                }
                // update n-1 elem id
                lastElemId = elem.getId();
            }
        }
    }

    /**
     * check filter data function with forbidden result
     *
     * @param argumentsAccessor - arguments
     * @throws Exception if something wrong
     */
    @ParameterizedTest
    @CsvSource({
            "unknown,dog,id,desc",
            "category,unknown,id,desc",
            "sex,female,unknown,desc",
            "sex,female,id,unknown"
    })
    public void FilterDataTest_Forbidden(ArgumentsAccessor argumentsAccessor) throws Exception {
        String filter = argumentsAccessor.getString(0);
        String filterBy = argumentsAccessor.getString(1);
        String sort = argumentsAccessor.getString(2);
        String sortBy = argumentsAccessor.getString(3);

        mockMvc.perform(get("/api/v1/test_task/filter")
                        .param("filter", filter)
                        .param("filterBy", filterBy)
                        .param("sort", sort)
                        .param("sortBy", sortBy))
                .andExpect(status().isForbidden());
    }
}
