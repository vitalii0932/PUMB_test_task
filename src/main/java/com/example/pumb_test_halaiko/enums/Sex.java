package com.example.pumb_test_halaiko.enums;

/**
 * animal sex enum
 */
public enum Sex {
    MALE, FEMALE;

    /**
     * check contains param in enum
     *
     * @param sex - checked sex value
     * @return true if sex value exist else false
     */
    public static boolean contains(String sex) {
        try {
            Sex.valueOf(sex);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
