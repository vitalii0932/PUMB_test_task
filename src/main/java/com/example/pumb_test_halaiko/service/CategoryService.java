package com.example.pumb_test_halaiko.service;

import com.example.pumb_test_halaiko.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * service class for Category
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<String> getAllNames() {
        List<String> allCategoriesNamesWithPrefix = categoryRepository.getAllCategoriesNames().stream()
                .map(name -> "category." + name)
                .collect(Collectors.toList());

        return allCategoriesNamesWithPrefix;

    }
}
