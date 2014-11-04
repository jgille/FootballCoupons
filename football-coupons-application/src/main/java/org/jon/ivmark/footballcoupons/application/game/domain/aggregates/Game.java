package org.jon.ivmark.footballcoupons.application.game.domain.aggregates;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jon.ivmark.footballcoupons.application.domain.aggregate.AggregateRoot;
import org.jon.ivmark.footballcoupons.application.game.domain.event.CouponSavedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameCreatedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends AggregateRoot<GameId> {

    private final String gameName;
    private final List<Coupon> coupons;

    private final List<GameEvent> uncommitedEvents;

    public Game(GameId gameId, String gameName) {
        super(gameId);
        this.gameName = gameName;
        this.uncommitedEvents = new ArrayList<>();
        this.coupons = new ArrayList<>();
    }

    public void saveCoupon(Coupon coupon) {
        coupons.add(coupon);
        addEvent(new CouponSavedEvent(getGameId(), coupon.getCouponId(), now(), coupon.getCouponName(),
                                      coupon.getCouponMustBeSubmittedBefore(), coupon.getMatches()));
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
        game.addEvent(new GameCreatedEvent(gameId, now(), gameName));
        return game;
    }

    private void addEvent(GameEvent gameEvent) {
        uncommitedEvents.add(gameEvent);
    }

    private static DateTime now() {
        // TODO: Use timeservice?
        return DateTime.now(DateTimeZone.UTC);
    }
}
