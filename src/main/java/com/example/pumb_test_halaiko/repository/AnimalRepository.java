package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * repository interface for Animal entities
 */
public interface AnimalRepository extends JpaRepository<Animal, Integer>{
    /**
     * delete all animal with some name
     *
     * @param name - animal name
     */
    void deleteAllByName(String name);
}
