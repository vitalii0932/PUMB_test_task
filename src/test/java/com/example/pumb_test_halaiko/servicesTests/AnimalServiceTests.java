package com.example.pumb_test_halaiko.servicesTests;

import com.example.pumb_test_halaiko.model.Animal;
import com.example.pumb_test_halaiko.repository.AnimalRepository;
import com.example.pumb_test_halaiko.repository.TypeRepository;
import com.example.pumb_test_halaiko.service.AnimalService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.Assert.fail;

/**
 * AnimalServicee tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AnimalServiceTests {

    @Autowired
    private AnimalService service;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private TypeRepository typeRepository;

    /**
     * save function test
     *
     * @param name - animal name
     * @param type - animal type
     * @param sex - animal sex
     * @param weight - animal weight
     * @param cost - animal cost
     * @param expectedCategoryId - expected category
     * @param expectedResult - expected result
     */
    @Transactional
    @ParameterizedTest
    @CsvSource({
            "test product, test_type, male, 10.0, 15.0, 1, true",
            "test product, test_type, male, 10.0, 35.0, 2, true",
            "test product, test_type, male, 10.0, 55.0, 3, true",
            "test product, test_type, male, 10.0, 75.0, 4, true",
            ", test_type, male, 10.0, 30.0, 0, false",
            "test product,, male, 10.0, 30.0, 0, false",
            "test product, test_type,, 10.0, 30.0, 0, false",
            "test product, test_type, male,, 30.0, 0, false",
            "test product, test_type, male, 10.0,, 0, false",
            "test product, test_type, uncorrect, 10.0, 30.0, 0, false",
            "test product, test_type, male, uncorrect, 30.0, 0, false",
            "test product, test_type, male, 10.0, uncorrect, 0, false"
    })
    public void saveTest(String name, String type, String sex,
                         String weight, String cost,
                         Integer expectedCategoryId, Boolean expectedResult) {
        // init values
        String[] animalParams = {name, type, sex, weight, cost};
        Animal savedAnimal = null;
        
        try {
            // try to save animal
            savedAnimal = service.save(animalParams);

            if (expectedResult) {
                // if expectedResult true check the expected category
                assert expectedCategoryId.equals(savedAnimal.getCategory().getId()) :
                        "Expected category ID: " + expectedCategoryId + ", but got: " + savedAnimal.getCategory().getId();
            } 
        } catch (IllegalArgumentException | NullPointerException ex) {
            // if something wrong
            if (expectedResult) {
                // if expected true
                fail("Expected successful save, but got exception: " + ex.getMessage());
            }
            ex.fillInStackTrace();
        } finally {
            // clean up
            if (savedAnimal != null) {
                animalRepository.deleteById(savedAnimal.getId());
                typeRepository.deleteById(savedAnimal.getType().getId());
            }
        }
    }
}
