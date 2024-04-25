package com.example.pumb_test_halaiko.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

/**
 * animal category data class
 */
@Data
@Entity(name = "Category")
public class Category {
    @Id
    @GeneratedValue
    @SequenceGenerator(name = "animal_sequence", sequenceName = "animal_sequence", allocationSize = 1)
    private Integer id;
    private String name;
}
