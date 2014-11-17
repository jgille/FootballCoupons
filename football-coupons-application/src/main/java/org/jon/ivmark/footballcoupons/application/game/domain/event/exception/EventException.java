package org.jon.ivmark.footballcoupons.application.game.domain.event.exception;

public class EventException extends RuntimeException {
    public EventException(String message, Exception cause) {
        super(message, cause);
    }
}
