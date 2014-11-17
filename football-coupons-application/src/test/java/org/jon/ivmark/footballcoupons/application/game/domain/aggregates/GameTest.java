package org.jon.ivmark.footballcoupons.application.game.domain.aggregates;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.jon.ivmark.footballcoupons.application.game.domain.event.CouponSavedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameCreatedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEventType;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.CouponId;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

public class GameTest {

    @Test
    public void createGame() {
        GameId gameId = GameId.randomGameId();
        String gameName = "Test";

        Game game = Game.createGame(gameId, gameName);

        assertThat(game.getGameId(), is(gameId));
        assertThat(game.getGameName(), is(gameName));
        assertThat(game.getSortedCoupons(), hasSize(0));
    }

    @Test
    public void assertThatTheCorrectEventIsAddedWhenCreatingGame() {
        GameId gameId = GameId.randomGameId();
        String gameName = "Test";

        Game game = Game.createGame(gameId, gameName);

        List<GameEvent> uncommitedEvents = game.getUncommitedEvents();
        assertThat(uncommitedEvents, hasSize(1));

        GameCreatedEvent gameCreatedEvent = (GameCreatedEvent) uncommitedEvents.get(0);
        assertThat(gameCreatedEvent.getAggregateId(), is(gameId.getValue()));
        assertThat(gameCreatedEvent.getGameName(), is(gameName));
        assertThat(gameCreatedEvent.getEventType(), is(GameEventType.GAME_CREATED));
        assertThat(gameCreatedEvent.getTimestamp(), notNullValue());
    }

    @Test
    public void insertCoupon() {
        GameId gameId = GameId.randomGameId();
        String gameName = "Test";
        Game game = Game.createGame(gameId, gameName);

        CouponId couponId = CouponId.randomCouponId();
        String couponName = "Coupon";
        DateTime mustBeSubmittedBefore = DateTime.now();
        List<Match> matches = new ArrayList<>();
        Coupon coupon = new Coupon(couponId, couponName, mustBeSubmittedBefore, matches);

        game.saveCoupon(coupon);

        assertThat(game.getCoupon(couponId), is(coupon));
        assertThat(game.getSortedCoupons(), hasSize(1));
        assertThat(game.getSortedCoupons().get(0), is(coupon));
    }

    @Test
    public void updateCoupon() {
        GameId gameId = GameId.randomGameId();
        String gameName = "Test";
        Game game = Game.createGame(gameId, gameName);

        CouponId couponId = CouponId.randomCouponId();
        String couponName = "Coupon";
        DateTime mustBeSubmittedBefore = DateTime.now();
        List<Match> matches = new ArrayList<>();
        Coupon coupon = new Coupon(couponId, couponName, mustBeSubmittedBefore, matches);

        Coupon updatedCoupon = new Coupon(couponId, couponName, mustBeSubmittedBefore.plusDays(1), matches);

        game.saveCoupon(coupon);
        game.saveCoupon(updatedCoupon);

        assertThat(game.getCoupon(couponId), is(updatedCoupon));
        assertThat(game.getSortedCoupons(), hasSize(1));
        assertThat(game.getSortedCoupons().get(0), is(updatedCoupon));
    }

    @Test
    public void insertMultipleCoupons() {
        GameId gameId = GameId.randomGameId();
        String gameName = "Test";
        Game game = Game.createGame(gameId, gameName);

        List<Match> matches = new ArrayList<>();
        Coupon firstCoupon = new Coupon(CouponId.randomCouponId(), "Coupon 1", DateTime.now(), matches);
        Coupon secondCoupon = new Coupon(CouponId.randomCouponId(), "Coupon 2", DateTime.now().minusDays(1), matches);

        game.saveCoupon(firstCoupon);
        game.saveCoupon(secondCoupon);

        assertThat(game.getCoupon(firstCoupon.getCouponId()), is(firstCoupon));
        assertThat(game.getCoupon(secondCoupon.getCouponId()), is(secondCoupon));
        assertThat(game.getSortedCoupons(), hasSize(2));
        assertThat(game.getSortedCoupons().get(0), is(secondCoupon));
        assertThat(game.getSortedCoupons().get(1), is(firstCoupon));
    }

    @Test
    public void assertThatTheCorrectEventIsAddedWhenSavingCoupon() {
        GameId gameId = GameId.randomGameId();
        String gameName = "Test";
        Game game = Game.createGame(gameId, gameName);

        CouponId couponId = CouponId.randomCouponId();
        String couponName = "Coupon";
        DateTime mustBeSubmittedBefore = DateTime.now();
        List<Match> matches = new ArrayList<>();
        Coupon coupon = new Coupon(couponId, couponName, mustBeSubmittedBefore, matches);

        game.saveCoupon(coupon);

        List<GameEvent> uncommitedEvents = game.getUncommitedEvents();
        assertThat(uncommitedEvents, hasSize(2));
        CouponSavedEvent couponSavedEvent = (CouponSavedEvent) uncommitedEvents.get(1);
        assertThat(couponSavedEvent.getAggregateId(), is(gameId.getValue()));
        assertThat(couponSavedEvent.getCouponId(), is(couponId.getValue()));
        assertThat(couponSavedEvent.getEventType(), is(GameEventType.COUPON_SAVED));
        assertThat(couponSavedEvent.getTimestamp(), notNullValue());
        assertThat(couponSavedEvent.getCouponMustBeSubmittedBefore(), is(mustBeSubmittedBefore));
        assertThat(couponSavedEvent.getCouponName(), is(couponName));
        assertThat(couponSavedEvent.getMatches(), hasSize(0));
    }

    @Test
    public void assertThatCommitClearsEvents() {
        GameId gameId = GameId.randomGameId();
        String gameName = "Test";
        Game game = Game.createGame(gameId, gameName);

        game.commit();

        assertThat(game.getUncommitedEvents(), hasSize(0));
    }

}
