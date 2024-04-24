package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository interface for Category entities
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
