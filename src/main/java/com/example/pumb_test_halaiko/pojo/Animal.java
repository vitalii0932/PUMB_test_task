package com.example.pumb_test_halaiko.pojo;

import com.example.pumb_test_halaiko.enums.Category;
import com.example.pumb_test_halaiko.enums.Sex;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private Sex sex;
    private Double weight;
    private Double cost;
    private Category category;
}
