package org.jon.ivmark.footballcoupons.application.game.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

public class GameCreatedEvent extends AbstractDomainEvent<GameEventType> implements GameEvent {
    private final String gameName;

    public GameCreatedEvent(@JsonProperty("aggregate_id") GameId gameId,
                            @JsonProperty("timestamp") DateTime createdAt,
                            @JsonProperty("game_name") String gameName) {
        super(gameId.getValue(), GameEventType.GAME_CREATED, createdAt);
        this.gameName = gameName;
    }

    @JsonProperty("game_name")
    public String getGameName() {
        return gameName;
    }
}
