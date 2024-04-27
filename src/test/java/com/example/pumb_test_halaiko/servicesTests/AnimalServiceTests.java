package com.example.pumb_test_halaiko.servicesTests;

import com.example.pumb_test_halaiko.model.Animal;
import com.example.pumb_test_halaiko.repository.AnimalRepository;
import com.example.pumb_test_halaiko.repository.TypeRepository;
import com.example.pumb_test_halaiko.service.AnimalService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * AnimalService tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AnimalServiceTests {

    @Autowired
    private AnimalService animalService;

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
            "test product, test_type, uncorrected, 10.0, 30.0, 0, false",
            "test product, test_type, male, uncorrected, 30.0, 0, false",
            "test product, test_type, male, 10.0, uncorrected, 0, false"
    })
    public void saveTest(String name, String type, String sex,
                         String weight, String cost,
                         Integer expectedCategoryId, Boolean expectedResult) {
        // init values
        String[] animalParams = {name, type, sex, weight, cost};
        Animal savedAnimal = null;
        
        try {
            // try to save animal
            savedAnimal = animalService.save(animalParams);

            assert !expectedResult || expectedCategoryId.equals(savedAnimal.getCategory().getId()) :
                    "Expected category ID: " + expectedCategoryId + ", but got: " + savedAnimal.getCategory().getId();
        } catch (IllegalArgumentException | NullPointerException ex) {
            // if something wrong
            if (expectedResult) {
                // if expected true
                Assertions.fail("Expected successful save, but got exception: " + ex.getMessage());
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

    /**
     * findAnimalsByParams function test
     *
     * @param filter - filter param
     * @param filterBy - filter value
     * @param sort - sort param
     * @param sortBy - sort type
     * @param expectedResult - expected result status
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
    public void findAnimalsByParamsTest(String filter, String filterBy, String sort, String sortBy, Boolean expectedResult) {
        try {
            // try to get data with params and check it
            checkFilteredData(filter, filterBy, sort, sortBy);
        } catch (RuntimeException ex) {
            // if something wrong
            if (expectedResult) {
                // if expected true
                Assertions.fail(String.format("Expected result was OK. Something wrong in case: {%s, %s, %s, %s}", filter, filterBy, sort, sortBy));
            }
            ex.fillInStackTrace();
        }
    }

    /**
     * check filtered data function
     *
     * @param filter - filter param
     * @param filterBy - filter value
     * @param sort - sort param
     * @param sortBy - sort type
     * @throws RuntimeException if something wrong
     */
    private void checkFilteredData(String filter, String filterBy, String sort, String sortBy) throws RuntimeException {
        List<Animal> responseList = animalService.findAnimalsByParams(filter, filterBy, sort, sortBy);

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