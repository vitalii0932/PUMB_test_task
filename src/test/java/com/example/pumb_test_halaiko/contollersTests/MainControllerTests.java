package com.example.pumb_test_halaiko.contollersTests;

import com.example.pumb_test_halaiko.model.Animal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
            "test.txt, text/plain, false",
            "empty.csv, text/csv, false",
            "empty.xml, text/xml, false",
            "random.csv, text/csv, false",
            "random.xml, text/xml, false"
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
        File file = ResourceUtils.getFile("classpath:test_files/" + fileName);

        FileInputStream input = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        input.read(bytes);
        input.close();

        return bytes;
    }

    /**
     * check filter data function
     *
     * @param filter - filter param
     * @param filterBy - filter value
     * @param sort - sort param
     * @param sortBy - sort type
     * @throws Exception if something wrong
     */
    @ParameterizedTest
    @CsvSource({
            "type,dog,id,desc,true",
            "category,First category,id,desc,true",
            "sex,female,id,desc,true",
            "unknown,dog,id,desc,false",
            "category,unknown,id,desc,false",
            "sex,female,unknown,desc,false",
            "sex,female,id,unknown,false",
            ",,,,false"
    })
    public void filterDataTest_Success(String filter, String filterBy, String sort, String sortBy, Boolean expectedResult) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/test_task/filter")
                        .param("filter", filter)
                        .param("filterBy", filterBy)
                        .param("sort", sort)
                        .param("sortBy", sortBy)) // perform a GET request with parameters
                .andExpect(expectedResult ? status().isOk() : status().is4xxClientError()) // expecting HTTP status 200 (OK)
                .andReturn(); // get the result of the performed action

        if (expectedResult) {
            String jsonResponse = mvcResult.getResponse().getContentAsString(); // extract JSON response
            List<Animal> responseList = new ObjectMapper().readValue(jsonResponse, new TypeReference<>() {
            });

            checkFilteredData(responseList, filter, filterBy, sortBy);
        }
    }

    /**
     * check filtered data function
     *
     * @param filter - filter param
     * @param filterBy - filter value
     * @param sortBy - sort type
     */
    private void checkFilteredData(List<Animal> responseList, String filter, String filterBy, String sortBy) {
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
}
