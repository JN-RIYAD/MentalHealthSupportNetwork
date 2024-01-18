package com.mhsn.riyad.enums;

import lombok.Getter;

@Getter
public enum Gender {
    Male("Male"),
    Female("Female"),
    Other("Other");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
