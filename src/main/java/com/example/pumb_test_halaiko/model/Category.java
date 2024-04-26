package com.example.pumb_test_halaiko.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * animal category data class
 */
@Data
@Entity(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "category_strategy", allocationSize = 1)
    private Integer id;
    private String name;
}
