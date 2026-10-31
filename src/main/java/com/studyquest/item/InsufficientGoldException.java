package com.studyquest.item;

public class InsufficientGoldException extends Exception {
    private static final long serialVersionUID = 1L;

    private int required;
    private int current;

    public InsufficientGoldException(int required, int current) {
        super("Gold tidak mencukupi! Dibutuhkan: " + required + ", Gold saat ini: " + current);
        this.required = required;
        this.current = current;
    }

    public int getRequired() {
        return this.required;
    }

    public int getCurrent() {
        return this.current;
    }

    @Override
    public String getMessage() {
        return "Gold tidak mencukupi! Dibutuhkan: " + this.required + ", Gold saat ini: " + this.current;
    }
}
