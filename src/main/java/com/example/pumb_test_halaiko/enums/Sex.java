package com.example.pumb_test_halaiko.enums;

/**
 * animal sex enum
 */
public enum Sex {
    MALE, FEMALE;

    public static boolean contains(String sex) {
        for (Sex value : values()) {
            if (value.name().equals(sex)) {
                return true;
            }
        }
        return false;
    }
}
