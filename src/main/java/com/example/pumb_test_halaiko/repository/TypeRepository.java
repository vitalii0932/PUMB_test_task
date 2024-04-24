package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * repository interface for Type entities
 */
public interface TypeRepository extends JpaRepository<Type, Integer> {
    /**
     * find optional Type by its name
     *
     * @param name - type name
     * @return optional Type
     */
    Optional<Type> findByName(String name);
}
