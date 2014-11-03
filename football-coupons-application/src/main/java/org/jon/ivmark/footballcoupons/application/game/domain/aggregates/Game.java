package org.jon.ivmark.footballcoupons.application.game.domain.aggregates;

import org.jon.ivmark.footballcoupons.application.domain.aggregate.AggregateRoot;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameCreatedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends AggregateRoot<GameId> {

    private final String gameName;
    private final List<GameEvent> uncommitedEvents;

    public Game(GameId gameId, String gameName) {
        super(gameId);
        this.gameName = gameName;
        this.uncommitedEvents = Collections.synchronizedList(new ArrayList<GameEvent>());
    }

    public void addCoupon(Coupon coupon) {
        // TODO: Implement
    }

    public GameId getGameId() {
        return getId();
    }

    public List<GameEvent> getUncommitedEvents() {
        return new ArrayList<>(uncommitedEvents);
    }

    public void commit() {
        uncommitedEvents.clear();
    }

    public static Game createGame(GameId gameId, String gameName) {
        Game game = new Game(gameId, gameName);
        game.addEvent(new GameCreatedEvent(gameId.getValue(), now(), gameName));
        return game;
    }

    private void addEvent(GameEvent gameEvent) {
        uncommitedEvents.add(gameEvent);
    }

    private static long now() {
        // TODO: Use timeservice?
        return System.currentTimeMillis();
    }
}
