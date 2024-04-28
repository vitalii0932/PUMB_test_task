package com.example.pumb_test_halaiko.servicesTests;

import com.example.pumb_test_halaiko.repository.CategoryRepository;
import com.example.pumb_test_halaiko.service.CategoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CategoryService tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryServiceTests {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryService service;

    /**
     * getAllNames function test
     */
    @Transactional
    @Test
    public void getAllNamesTest() {
        var allTypesNames = repository.getAllCategoriesNames();
        var allTypesNamesWithPrefix = service.getAllNames();

        // null checks
        assert (allTypesNames != null && allTypesNamesWithPrefix != null) : "Lists should not be null";

        // empty lists handling
        assert (!allTypesNames.isEmpty() && !allTypesNamesWithPrefix.isEmpty()) : "Lists should not be empty";

        // size check
        assert (allTypesNames.size() == allTypesNamesWithPrefix.size()) : "Lists should have equal sizes";

        for (int i = 0; i < allTypesNames.size(); i++) {
            // comparison
            assert (allTypesNamesWithPrefix.get(i).equals("category." + allTypesNames.get(i))) :
                    "Mismatch at index " + i + ": expected 'category." + allTypesNames.get(i) + "', found '" + allTypesNamesWithPrefix.get(i) + "'";
        }
    }
}
