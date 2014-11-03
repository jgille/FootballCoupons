package org.jon.ivmark.footballcoupons.application.game.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameCreatedEvent extends AbstractDomainEvent<GameEventType> implements GameEvent {
    private final String gameName;

    public GameCreatedEvent(@JsonProperty("aggregate_id") String gameId,
                            @JsonProperty("timestamp") long createdAt,
                            @JsonProperty("game_name") String gameName) {
        super(gameId, GameEventType.GAME_CREATED, createdAt);
        this.gameName = gameName;
    }

    @JsonProperty("game_name")
    public String getGameName() {
        return gameName;
    }
}
