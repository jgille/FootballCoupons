package org.jon.ivmark.footballcoupons.application.game.domain.aggregates;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jon.ivmark.footballcoupons.application.domain.aggregate.AggregateRoot;
import org.jon.ivmark.footballcoupons.application.game.domain.event.CouponSavedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameCreatedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.MatchDto;
import org.jon.ivmark.footballcoupons.application.game.domain.exception.NoSuchCouponException;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.CouponId;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Game extends AggregateRoot<GameId> {

    private final String gameName;
    private final Map<CouponId, Coupon> coupons;

    private final List<GameEvent> uncommitedEvents;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Game(GameId gameId, String gameName) {
        super(gameId);
        this.gameName = gameName;
        this.uncommitedEvents = new ArrayList<>();
        this.coupons = new HashMap<>();
    }

    public void saveCoupon(Coupon coupon) {
        CouponId couponId = coupon.getCouponId();
        if (coupons.containsKey(couponId)) {
            logger.debug("Updating existing coupon: '{}' for game '{}'", couponId.getValue(), getGameId().getValue());
        } else {
            logger.debug("Creating new coupon: '{}' for game '{}'", couponId.getValue(), getGameId().getValue());
        }
        coupons.put(couponId, coupon);
        addEvent(new CouponSavedEvent(getGameId(), couponId, now(), coupon.getCouponName(),
                                      coupon.getCouponMustBeSubmittedBefore(), toMatchDtos(coupon.getMatches())));
    }

    private List<MatchDto> toMatchDtos(List<Match> matches) {
        List<MatchDto> dtos = new ArrayList<>(matches.size());
        for (Match match : matches) {
            MatchDto matchDto = new MatchDto();
            matchDto.match_index = match.getMatchId().getMatchIndex();
            matchDto.home_team = match.getHomeTeam();
            matchDto.away_team = match.getAwayTeam();
            matchDto.matchResult = match.getMatchResult();
        }
        return dtos;
    }

    public List<Coupon> getSortedCoupons() {
        List<Coupon> couponList = new ArrayList<>(coupons.values());
        Collections.sort(couponList, new CouponComparator());
        return couponList;
    }

    public Coupon getCoupon(CouponId couponId) {
        Coupon coupon = coupons.get(couponId);
        if (coupon == null) {
            throw new NoSuchCouponException(getGameId(), couponId);
        }
        return coupon;
    }

    public GameId getGameId() {
        return getId();
    }

    public String getGameName() {
        return gameName;
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

    static class CouponComparator implements Comparator<Coupon> {
        @Override
        public int compare(Coupon c1, Coupon c2) {
            DateTime mustBeSubmittedBefore1 = c1.getCouponMustBeSubmittedBefore();
            DateTime couponMustBeSubmittedBefore2 = c2.getCouponMustBeSubmittedBefore();
            if (mustBeSubmittedBefore1.isBefore(couponMustBeSubmittedBefore2)) {
                return -1;
            } else if (mustBeSubmittedBefore1.isAfter(couponMustBeSubmittedBefore2)) {
                return 1;
            }
            return 0;
        }
    }
}
