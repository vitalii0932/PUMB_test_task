package com.example.pumb_test_halaiko.servicesTests;

import com.example.pumb_test_halaiko.repository.AnimalRepository;
import com.example.pumb_test_halaiko.service.FileService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * FileService tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class FileServiceTests {

    @Autowired
    private FileService fileService;

    @Autowired
    private AnimalRepository animalRepository;

    /**
     * readFile function test
     *
     * @param fileName - file name
     * @param contentType - file content type
     * @param addedAnimalsCount - number of data that can be added to the file
     * @throws Exception if something wrong
     */
    @ParameterizedTest
    @CsvSource({
            "animals.csv, text/csv, 6",
            "animals.xml, text/xml, 7",
            "test.txt, text/plain, 0",
            "empty.csv, text/csv, 0",
            "empty.xml, text/xml, 0",
            "random.csv, text/csv, 0",
            "random.xml, text/xml, 0"
    })
    public void readFileTest(String fileName, String contentType, Long addedAnimalsCount) throws Exception {
        // current animals count in db
        Long currentAnimalsCount = animalRepository.count();
        // read bytes from file
        byte[] fileBytes = readFileFromResources(fileName);
        MultipartFile file = new MockMultipartFile("file", fileName, contentType, fileBytes);

        // try to save this file
        try {
            fileService.readFile(file);
        } catch (IOException ex) {
            // throw if extension is not supported
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }

        // new animal count in db after adding new data
        Long newAnimalsCount = animalRepository.count();

        // check the count
        assert (newAnimalsCount - currentAnimalsCount == addedAnimalsCount);
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
}
