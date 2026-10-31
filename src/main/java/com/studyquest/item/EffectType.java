package com.studyquest.item;

public enum EffectType {
    HEAL_HP("Heal HP"),
    RESTORE_MP("Restore MP"),
    XP_BOOST("XP Boost");

    private final String label;

    EffectType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
