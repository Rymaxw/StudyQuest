package com.studyquest.item;

public class InsufficientGoldException extends Exception {
    private final int required;
    private final int current;

    public InsufficientGoldException(int required, int current) {
        super("Gold tidak cukup! Dibutuhkan: " + required + ", Tersedia: " + current);
        this.required = required;
        this.current = current;
    }

    public int getRequired() { return required; }
    public int getCurrent() { return current; }
}
