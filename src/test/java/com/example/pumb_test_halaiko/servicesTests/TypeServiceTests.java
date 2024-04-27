package com.example.pumb_test_halaiko.servicesTests;

import com.example.pumb_test_halaiko.repository.TypeRepository;
import com.example.pumb_test_halaiko.service.TypeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * TypeService tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TypeServiceTests {

    @Autowired
    private TypeRepository repository;

    @Autowired
    private TypeService service;

    /**
     * getAllNames function test
     */
    @Transactional
    @Test
    public void getAllNamesTest() {
        var allTypesNames = repository.getAllTypesNames();
        var allTypesNamesWithPrefix = service.getAllNames();

        // null checks
        assert (allTypesNames != null && allTypesNamesWithPrefix != null) : "Lists should not be null";

        // empty lists handling
        assert (!allTypesNames.isEmpty() && !allTypesNamesWithPrefix.isEmpty()) : "Lists should not be empty";

        // size check
        assert (allTypesNames.size() == allTypesNamesWithPrefix.size()) : "Lists should have equal sizes";

        for (int i = 0; i < allTypesNames.size(); i++) {
            // comparison
            assert (allTypesNamesWithPrefix.get(i).equals("type." + allTypesNames.get(i))) :
                    "Mismatch at index " + i + ": expected 'type." + allTypesNames.get(i) + "', found '" + allTypesNamesWithPrefix.get(i) + "'";
        }
    }
}
