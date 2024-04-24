package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
