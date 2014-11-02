package org.jon.ivmark.footballcoupons.application.game.domain.aggregates;

import org.jon.ivmark.footballcoupons.application.domain.aggregate.AggregateRoot;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

import java.util.Collections;
import java.util.List;

public class Game extends AggregateRoot<GameId> {

    public Game(GameId gameId) {
        super(gameId);
    }

    public void addCoupon(Coupon coupon) {
        // TODO: Implement
    }

    public GameId getGameId() {
        return getId();
    }

    public List<GameEvent> getUncommitedEvents() {
        // TODO: Implement
        return Collections.emptyList();
    }
}
