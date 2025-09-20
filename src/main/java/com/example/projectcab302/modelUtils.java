package com.example.projectcab302;

public class modelUtils {
    private modelUtils() {} // no instances

    public static String checkValidityAndTrim(String input, String name) {
        if (input == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be blank");
        }
        return trimmed;
    }
}
