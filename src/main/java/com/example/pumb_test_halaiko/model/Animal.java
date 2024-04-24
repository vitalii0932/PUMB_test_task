package com.example.pumb_test_halaiko.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * simple animal data class
 */
@Data
@Entity(name = "Animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animal_sequence")
    @SequenceGenerator(name = "animal_sequence", sequenceName = "animal_sequence", allocationSize = 1)
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
