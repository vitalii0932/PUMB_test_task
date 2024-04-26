package com.example.pumb_test_halaiko.repositoriesTests;

import com.example.pumb_test_halaiko.model.Animal;
import com.example.pumb_test_halaiko.model.Category;
import com.example.pumb_test_halaiko.model.Type;
import com.example.pumb_test_halaiko.repository.AnimalRepository;
import com.example.pumb_test_halaiko.repository.CategoryRepository;
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
 * AnimalRepo tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AnimalRepositoryTests {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TypeRepository typeRepository;

    private Animal testAnimal;

    /**
     * set the test animal before all the tests
     */
    @BeforeEach
    public void setUpTheTestAnimal() {
        testAnimal = new Animal();
        Category category = categoryRepository.findByName("First category").get();
        Type type = typeRepository.findByName("cat").get();
        testAnimal.setName("test");
        testAnimal.setCategory(category);
        testAnimal.setType(type);
        testAnimal.setCost(1.0);
        testAnimal.setWeight(1.0);
        testAnimal.setSex("male");
    }

    /**
     * delete all the test animals
     */
    @AfterEach
    public void deleteTheTestAnimal() {
        animalRepository.deleteAllByName(testAnimal.getName());
    }

    /**
     * deleteAllByName function test
     */
    @Test
    @Transactional
    public void deleteAllByNameTest() {
        // set up the animals with the same name
        Animal savedAnimal1 = animalRepository.save(testAnimal);
        Animal savedAnimal2 = animalRepository.save(testAnimal);
        Animal savedAnimal3 = animalRepository.save(testAnimal);

        List<Animal> savedAnimals = List.of(savedAnimal1, savedAnimal2, savedAnimal3);

        // save all the test animals in the db
        for (var savedAnimal : savedAnimals) {
            assert (animalRepository.findById(savedAnimal.getId()).isPresent());
        }

        // delete all the test animal by their name
        animalRepository.deleteAllByName(testAnimal.getName());

        // checking that the animals are not in the database
        for (var savedAnimal : savedAnimals) {
            assert (animalRepository.findById(savedAnimal.getId()).isEmpty());
        }
    }

    /**
     * check the insert operation
     */
    @Test
    @Transactional
    public void insertTest() {
        // checking the number of animals prior to conservation
        long countBefore = animalRepository.count();

        // saving an animal in the database
        Animal savedAnimal = animalRepository.save(testAnimal);

        // checks
        assert (countBefore == animalRepository.count() - 1);
        assert (animalRepository.existsById(savedAnimal.getId()));
        assert (savedAnimal.equals(testAnimal));
    }

    /**
     * check the update operation
     */
    @Test
    @Transactional
    public void updateTest() {
        var savedAnimal = animalRepository.save(testAnimal);
        var oldCost = savedAnimal.getCost();

        savedAnimal.setCost(10.0);

        var updatedAnimal = animalRepository.save(savedAnimal);

        assert (Objects.equals(updatedAnimal.getId(), savedAnimal.getId()));
        assert (!Objects.equals(updatedAnimal.getCost(), oldCost));
    }

    /**
     * check the select operation
     */
    @Test
    @Transactional
    public void selectTest() {
        assert (!animalRepository.findAll().isEmpty());
    }

    /**
     * check the delete operation
     */
    @Test
    @Transactional
    public void deleteTest() {
        var savedAnimal = animalRepository.save(testAnimal);

        animalRepository.deleteById(savedAnimal.getId());

        assert (animalRepository.findById(savedAnimal.getId()).isEmpty());
    }
}
