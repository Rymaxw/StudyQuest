package com.studyquest.item;

public final class InventoryFullException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InventoryFullException(String message) { super(message); }
}
