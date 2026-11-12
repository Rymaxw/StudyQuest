package com.studyquest.item;

public final class InsufficientGoldException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InsufficientGoldException(String message) { super(message); }
}
