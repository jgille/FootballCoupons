package org.jon.ivmark.footballcoupons.application.game.infrastructure.event;

public class EventLogException extends RuntimeException {
    public EventLogException(String message, Exception cause) {
        super(message, cause);
    }
}
