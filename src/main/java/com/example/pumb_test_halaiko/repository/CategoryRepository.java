package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);

    @Query(value = "select name from category", nativeQuery = true)
    List<String> getAllCategoriesNames();
}
