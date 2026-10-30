package com.studyquest.system;

import java.io.Serializable;
import java.time.LocalDate;

public class GameEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final EventType type;
    private final int value;
    private final LocalDate date;

    public GameEvent(EventType type, int value, LocalDate date) {
        this.type = type;
        this.value = value;
        this.date = date;
    }

    public EventType getType() { return type; }
    public int getValue() { return value; }
    public LocalDate getDate() { return date; }
}
