package com.example.pumb_test_halaiko.repository;

import com.example.pumb_test_halaiko.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * repository interface for User entities
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * find optional user by his name
     *
     * @param email - user name
     * @return optional user
     */
    Optional<User> findUserByEmail(String email);
}
