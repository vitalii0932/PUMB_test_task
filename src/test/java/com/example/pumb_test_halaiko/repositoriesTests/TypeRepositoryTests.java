package com.example.pumb_test_halaiko.repositoriesTests;

import com.example.pumb_test_halaiko.model.Type;
import com.example.pumb_test_halaiko.repository.TypeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

/**
 * TypeRepo tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TypeRepositoryTests {
    @Autowired
    private TypeRepository typeRepository;

    private Type testType;

    /**
     * set the test type before all the tests
     */
    @BeforeEach
    public void setUpTheTestType() {
        testType = new Type();
        testType.setName("test type");
    }

    /**
     * delete all the test types
     */
    @AfterEach
    public void deleteTheTestType() {
        typeRepository.deleteAllByName(testType.getName());
    }

    /**
     * deleteAllByName function test
     */
    @Test
    @Transactional
    public void deleteAllByNameTest() {
        // set up the types with the same name
        Type savedType1 = typeRepository.save(testType);
        Type savedType2 = typeRepository.save(testType);
        Type savedType3 = typeRepository.save(testType);

        List<Type> savedTypes = List.of(savedType1, savedType2, savedType3);

        // save all the test types in the db
        for (var savedType : savedTypes) {
            assert (typeRepository.findById(savedType.getId()).isPresent());
        }

        // delete all the test type by their name
        typeRepository.deleteAllByName(testType.getName());

        // checking that the types are not in the database
        for (var savedType : savedTypes) {
            assert (typeRepository.findById(savedType.getId()).isEmpty());
        }
    }

    /**
     * findByName function test
     */
    @Test
    @Transactional
    public void findByNameTest() {
        // save the test type in db
        Type savedType = typeRepository.save(testType);

        // find this type
        Type foundType = typeRepository.findByName(savedType.getName())
                .orElseThrow(() -> new AssertionError("Type not found"));

        // check the found type
        assert(savedType.equals(foundType));
    }

    /**
     * getAllTypesNames function tests
     */
    @Test
    @Transactional
    public void getAllTypesNamesTest() {
        // get all the types
        var allTypes = typeRepository.findAll();
        // get all the types names
        var allTypesNames = typeRepository.getAllTypesNames();

        // check all the types names
        assert(allTypesNames.containsAll(
                allTypes.stream()
                        .map(Type::getName)
                        .toList()
        ));
    }

    /**
     * check the insert operation
     */
    @Test
    @Transactional
    public void insertTest() {
        // checking the number of types prior to conservation
        long countBefore = typeRepository.count();

        // saving a type in the database
        Type savedType = typeRepository.save(testType);

        // checks
        assert (countBefore == typeRepository.count() - 1);
        assert (typeRepository.existsById(savedType.getId()));
        assert (savedType.equals(testType));
    }

    /**
     * check the update operation
     */
    @Test
    @Transactional
    public void updateTest() {
        var savedType = typeRepository.save(testType);

        savedType.setName("TEST TYPE");

        var updatedType = typeRepository.save(savedType);

        assert (Objects.equals(updatedType.getId(), savedType.getId()));
        assert (Objects.equals(updatedType.getName(), "TEST TYPE"));
    }

    /**
     * check the select operation
     */
    @Test
    @Transactional
    public void selectTest() {
        assert (!typeRepository.findAll().isEmpty());
    }

    /**
     * check the delete operation
     */
    @Test
    @Transactional
    public void deleteTest() {
        var savedType = typeRepository.save(testType);

        typeRepository.deleteById(savedType.getId());

        assert (typeRepository.findById(savedType.getId()).isEmpty());
    }
}
