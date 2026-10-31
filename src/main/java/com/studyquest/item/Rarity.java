package com.studyquest.item;

public enum Rarity {
    COMMON("Common"),
    RARE("Rare"),
    EPIC("Epic");

    private final String label;

    Rarity(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
