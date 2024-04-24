package com.example.pumb_test_halaiko.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * animal category data class
 */
@Data
@Entity(name = "Category")
public class Category {
    @Id
    private Integer id;
    private String name;
}
