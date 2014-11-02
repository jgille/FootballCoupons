package org.jon.ivmark.footballcoupons.application.game.domain.exception;

import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

public class NoSuchGameException extends IllegalArgumentException {
    public NoSuchGameException(GameId gameId) {
        super(String.format("No game found with id: %s", gameId.getValue()));
    }
}
