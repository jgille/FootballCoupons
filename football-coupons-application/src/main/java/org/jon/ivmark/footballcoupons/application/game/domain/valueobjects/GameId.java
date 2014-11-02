package org.jon.ivmark.footballcoupons.application.game.domain.valueobjects;

import java.util.UUID;

public class GameId extends AggregateId {
    public GameId(String value) {
        super(value);
    }

    public static GameId randomGameId() {
        return new GameId(UUID.randomUUID().toString());
    }
}
