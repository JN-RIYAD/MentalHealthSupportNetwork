package com.mhsn.riyad.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    Normal("Normal"),
    Therapist("Therapist"),
    Admin("Admin");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
