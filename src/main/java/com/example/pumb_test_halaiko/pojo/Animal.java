package com.example.pumb_test_halaiko.pojo;

import com.example.pumb_test_halaiko.enums.Category;
import com.example.pumb_test_halaiko.enums.Sex;
import lombok.Getter;
import lombok.Setter;

/**
 * simple animal data class
 */
@Getter
@Setter
public class Animal {
    private String name;
    private Type type;
    private Sex sex;
    private Double weight;
    private Double cost;
    private Category category;
}
