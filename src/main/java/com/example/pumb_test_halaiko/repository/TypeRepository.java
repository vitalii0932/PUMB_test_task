package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * repository interface for Type entities
 */
public interface TypeRepository extends JpaRepository<Type, Integer> {
    /**
     * delete all types with some name
     *
     * @param name - type name
     */
    void deleteAllByName(String name);
    /**
     * find optional Type by its name
     *
     * @param name - type name
     * @return optional Type
     */
    Optional<Type> findByName(String name);

    /**
     * get all types names function
     *
     * @return a list on names
     */
    @Query(value = "select name from type", nativeQuery = true)
    List<String> getAllTypesNames();
}
