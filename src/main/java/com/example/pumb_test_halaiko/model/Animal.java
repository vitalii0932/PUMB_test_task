package com.example.pumb_test_halaiko.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * simple animal data class
 */
@Data
@Entity(name = "Animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "animal_strategy", allocationSize = 1)
    private Integer id;
    private String name;
    @ManyToOne
    private Type type;
    private String sex;
    private Double weight;
    private Double cost;
    @ManyToOne
    private Category category;

    /**
     * get fields from Animal class
     *
     * @return a list of strings
     */
    public static List<String> getAnimalFields() {
        return Arrays.stream(Animal.class.getDeclaredFields())
                .map(field -> field.getName())
                .collect(Collectors.toList());
    }
}
