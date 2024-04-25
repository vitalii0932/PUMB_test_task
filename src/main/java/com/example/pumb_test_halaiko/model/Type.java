package com.example.pumb_test_halaiko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * simple animal type data class
 */
@Entity(name = "Type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "animal_sequence", sequenceName = "animal_sequence", allocationSize = 1)
    private Integer id;
    private String name;
}
