package com.example.pumb_test_halaiko.repositoriesTests;

import com.example.pumb_test_halaiko.model.User;
import com.example.pumb_test_halaiko.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

/**
 * UserRepo tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    /**
     * set the test user before all the tests
     */
    @BeforeEach
    public void setUpTheTestUser() {
        testUser = new User();
        testUser.setEmail("test_user@com.ua");
        testUser.setPassword("supersecret!");
    }

    /**
     * delete all the test users
     */
    @AfterEach
    public void deleteTheTestUser() {
        userRepository.deleteByEmail(testUser.getEmail());
    }

    /**
     * deleteAllByName function test
     */
    @Test
    @Transactional
    public void deleteAllByNameTest() {
        // set up the users with the same name
        User savedUser1 = userRepository.save(testUser);
        User savedUser2 = userRepository.save(testUser);
        User savedUser3 = userRepository.save(testUser);

        List<User> savedUsers = List.of(savedUser1, savedUser2, savedUser3);

        // save all the test users in the db
        for (var savedUser : savedUsers) {
            assert (userRepository.findById(savedUser.getId()).isPresent());
        }

        // delete all the test user by their name
        userRepository.deleteByEmail(testUser.getEmail());

        // checking that the users are not in the database
        for (var savedUser : savedUsers) {
            assert (userRepository.findById(savedUser.getId()).isEmpty());
        }
    }

    /**
     * findByName function test
     */
    @Test
    @Transactional
    public void findByNameTest() {
        // save the test user in db
        User savedUser = userRepository.save(testUser);

        // find this user
        User foundUser = userRepository.findUserByEmail(savedUser.getEmail())
                .orElseThrow(() -> new AssertionError("User not found"));

        // check the found user
        assert(savedUser.equals(foundUser));
    }

    /**
     * check the insert operation
     */
    @Test
    @Transactional
    public void insertTest() {
        // checking the number of users prior to conservation
        long countBefore = userRepository.count();

        // saving a user in the database
        User savedUser = userRepository.save(testUser);

        // checks
        assert (countBefore == userRepository.count() - 1);
        assert (userRepository.existsById(savedUser.getId()));
        assert (savedUser.equals(testUser));
    }

    /**
     * check the update operation
     */
    @Test
    @Transactional
    public void updateTest() {
        var savedUser = userRepository.save(testUser);

        savedUser.setEmail("new_test_email@com.ua");

        var updatedUser = userRepository.save(savedUser);

        assert (Objects.equals(updatedUser.getId(), savedUser.getId()));
        assert (Objects.equals(updatedUser.getEmail(), "new_test_email@com.ua"));
    }

    /**
     * check the select operation
     */
    @Test
    @Transactional
    public void selectTest() {
        assert (!userRepository.findAll().isEmpty());
    }

    /**
     * check the delete operation
     */
    @Test
    @Transactional
    public void deleteTest() {
        var savedUser = userRepository.save(testUser);

        userRepository.deleteById(savedUser.getId());

        assert (userRepository.findById(savedUser.getId()).isEmpty());
    }
}
