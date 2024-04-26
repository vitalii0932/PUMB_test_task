package com.example.pumb_test_halaiko.repositoriesTests;

import com.example.pumb_test_halaiko.model.Category;
import com.example.pumb_test_halaiko.model.Category;
import com.example.pumb_test_halaiko.model.Type;
import com.example.pumb_test_halaiko.repository.CategoryRepository;
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
import java.util.stream.Collectors;

/**
 * CategoryRepo tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    /**
     * set the test category before all the tests
     */
    @BeforeEach
    public void setUpTheTestCategory() {
        testCategory = new Category();
        testCategory.setName("test category");
    }

    /**
     * delete all the test categories
     */
    @AfterEach
    public void deleteTheTestCategory() {
        categoryRepository.deleteAllByName(testCategory.getName());
    }

    /**
     * deleteAllByName function test
     */
    @Test
    @Transactional
    public void deleteAllByNameTest() {
        // set up the categories with the same name
        Category savedCategory1 = categoryRepository.save(testCategory);
        Category savedCategory2 = categoryRepository.save(testCategory);
        Category savedCategory3 = categoryRepository.save(testCategory);

        List<Category> savedCategories = List.of(savedCategory1, savedCategory2, savedCategory3);

        // save all the test categories in the db
        for (var savedCategory : savedCategories) {
            assert (categoryRepository.findById(savedCategory.getId()).isPresent());
        }

        // delete all the test category by their name
        categoryRepository.deleteAllByName(testCategory.getName());

        // checking that the categories are not in the database
        for (var savedCategory : savedCategories) {
            assert (categoryRepository.findById(savedCategory.getId()).isEmpty());
        }
    }

    /**
     * findByName function test
     */
    @Test
    @Transactional
    public void findByNameTest() {
        // save the test category in db
        Category savedCategory = categoryRepository.save(testCategory);

        // find this category
        Category foundCategory = categoryRepository.findByName(savedCategory.getName())
                .orElseThrow(() -> new AssertionError("Category not found"));

        // check the found category
        assert(savedCategory.equals(foundCategory));
    }

    /**
     * getAllCategoriesNames function tests
     */
    @Test
    @Transactional
    public void getAllCategoriesNamesTest() {
        // get all the categories
        var allCategories = categoryRepository.findAll();
        // get all the categories names
        var allCategoriesNames = categoryRepository.getAllCategoriesNames();

        // check all the categories names
        assert(allCategoriesNames.containsAll(
                allCategories.stream()
                        .map(Category::getName)
                        .toList()
        ));
    }

    /**
     * check the insert operation
     */
    @Test
    @Transactional
    public void insertTest() {
        // checking the number of categories prior to conservation
        long countBefore = categoryRepository.count();

        // saving a category in the database
        Category savedCategory = categoryRepository.save(testCategory);

        // checks
        assert (countBefore == categoryRepository.count() - 1);
        assert (categoryRepository.existsById(savedCategory.getId()));
        assert (savedCategory.equals(testCategory));
    }

    /**
     * check the update operation
     */
    @Test
    @Transactional
    public void updateTest() {
        var savedCategory = categoryRepository.save(testCategory);
        var oldName = savedCategory.getName();

        savedCategory.setName("TEST CATEGORY");

        var updatedCategory = categoryRepository.save(savedCategory);

        assert (Objects.equals(updatedCategory.getId(), savedCategory.getId()));
        assert (!Objects.equals(updatedCategory.getName(), oldName));
    }

    /**
     * check the select operation
     */
    @Test
    @Transactional
    public void selectTest() {
        assert (!categoryRepository.findAll().isEmpty());
    }

    /**
     * check the delete operation
     */
    @Test
    @Transactional
    public void deleteTest() {
        var savedCategory = categoryRepository.save(testCategory);

        categoryRepository.deleteById(savedCategory.getId());

        assert (categoryRepository.findById(savedCategory.getId()).isEmpty());
    }
}
