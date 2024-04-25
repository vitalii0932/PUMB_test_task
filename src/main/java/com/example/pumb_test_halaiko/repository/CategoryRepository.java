package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * repository interface for Category entities
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * get optional category by its name
     *
     * @param name - category name
     * @return optional category
     */
    Optional<Category> findByName(String name);

    /**
     * get all categories names function
     *
     * @return a list of categories names
     */
    @Query(value = "select name from category", nativeQuery = true)
    List<String> getAllCategoriesNames();
}
