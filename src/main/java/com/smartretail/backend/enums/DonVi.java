package com.smartretail.backend.enums;

import lombok.Getter;

@Getter
public enum DonVi {
    CAI(1, "Cái"),
    LON(2, "Lon"),
    CHAI(3, "Chai"),
    KG(4, "Kg"),
    THUNG(5, "Thùng");

    private final int id;
    private final String label;

    DonVi(int id, String label) {
        this.id = id;
        this.label = label;
    }
}