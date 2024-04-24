package com.example.pumb_test_halaiko.model;

import com.example.pumb_test_halaiko.enums.Sex;
import jakarta.persistence.*;
import lombok.Data;

/**
 * simple animal data class
 */
@Data
@Entity(name = "Animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;
    @ManyToOne
    private Type type;
    private String sex;
    private Double weight;
    private Double cost;
    @ManyToOne
    private Category category;
}
