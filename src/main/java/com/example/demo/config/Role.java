package com.example.demo.config;

public enum Role {
    INSURER,
    PARTSSHOP,
    EXAMINER;

    public static Role fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
