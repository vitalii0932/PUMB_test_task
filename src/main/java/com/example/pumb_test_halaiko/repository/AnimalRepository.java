package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.pojo.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository interface for Animal entities
 */
public interface AnimalRepository extends JpaRepository<Animal, Integer> {
}
