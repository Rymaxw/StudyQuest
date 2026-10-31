package com.studyquest.item;

public enum EquipSlot {
    WEAPON("Weapon"),
    ARMOR("Armor"),
    ACCESSORY("Accessory");

    private final String label;

    EquipSlot(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
