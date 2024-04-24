package com.example.pumb_test_halaiko.service;

import com.example.pumb_test_halaiko.enums.Sex;
import com.example.pumb_test_halaiko.model.Animal;
import com.example.pumb_test_halaiko.model.Category;
import com.example.pumb_test_halaiko.model.Type;
import com.example.pumb_test_halaiko.repository.AnimalRepository;
import com.example.pumb_test_halaiko.repository.CategoryRepository;
import com.example.pumb_test_halaiko.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;

/**
 * service class for Animal
 */
@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final CategoryRepository categoryRepository;
    private final TypeRepository typeRepository;

    /**
     * read file and save the correct data
     *
     * @param file - file with data about animals from user
     * @throws IOException
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void readFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileExtension = null;
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        
        if (fileExtension != null && fileExtension.equals("csv")) {
            readFromCsv(file);
        } else if (fileExtension != null && fileExtension.equals("xml")) {
        }
    }

    /**
     * read the file and save the correct data
     *
     * @param file - file with animal data from user
     * @throws IOException if something is wrong when reading the file
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void readFromCsv(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] elements = Arrays.stream(line.split(","))
                        .filter(str -> !str.isEmpty())
                        .toArray(String[]::new);
                if (elements.length == 5) {
                    try {
                        save(elements);
                    } catch (IllegalArgumentException ex) {
                        ex.fillInStackTrace();
                    }
                }
            }
        }
    }

    /**
     * save new animal with some data
     *
     * @param params - data from users file
     * @throws IllegalArgumentException if data is incorrect
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void save(String[] params) throws IllegalArgumentException {
        Animal animal = new Animal();
        animal.setName(params[0]);

        var type = typeRepository.findByName(params[1])
                .orElseGet(() -> typeRepository.save(Type.builder().name(params[1]).build()));

        animal.setType(type);

        params[2] = params[2].toUpperCase();

        if (Sex.contains(params[2])) {
            animal.setSex(params[2].toLowerCase());
        }

        animal.setWeight(Double.parseDouble(params[3]));
        animal.setCost(Double.parseDouble(params[4]));

        if (animal.getCost() > 0 && animal.getCost() <= 20) {
            animal.setCategory(categoryRepository.findById(1).orElseThrow());
        } else if (animal.getCost() > 20 && animal.getCost() <= 40) {
            animal.setCategory(categoryRepository.findById(2).orElseThrow());
        } else if (animal.getCost() > 40 && animal.getCost() <= 60) {
            animal.setCategory(categoryRepository.findById(3).orElseThrow());
        } else if (animal.getCost() > 60) {
            animal.setCategory(categoryRepository.findById(4).orElseThrow());
        } else {
            throw new NumberFormatException("price must be greater then 0");
        }

        animalRepository.save(animal);
    }
}
