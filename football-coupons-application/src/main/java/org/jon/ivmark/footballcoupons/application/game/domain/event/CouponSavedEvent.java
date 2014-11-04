package org.jon.ivmark.footballcoupons.application.game.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Match;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.CouponId;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

import java.util.List;

public class CouponSavedEvent extends AbstractDomainEvent<GameEventType> implements GameEvent {
    private final CouponId couponId;
    private final String couponName;
    private final DateTime couponMustBeSubmittedBefore;
    // TODO: Don't use matches directly
    private final List<Match> matches;

    public CouponSavedEvent(
            @JsonProperty("aggregate_id") GameId gameId,
            @JsonProperty("coupon_id") CouponId couponId,
            @JsonProperty("timestamp") DateTime savedAt,
            @JsonProperty("coupon_name") String couponName,
            @JsonProperty("must_be_submitted_before") DateTime couponMustBeSubmittedBefore,
            @JsonProperty("matches") List<Match> matches) {
        super(gameId.getValue(), GameEventType.COUPON_SAVED, savedAt);
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponMustBeSubmittedBefore = couponMustBeSubmittedBefore;
        this.matches = matches;
    }

    @JsonProperty("coupon_id")
    public CouponId getCouponId() {
        return couponId;
    }

    @JsonProperty("coupon_name")
    public String getCouponName() {
        return couponName;
    }

    @JsonProperty("must_be_submitted_before")
    public DateTime getCouponMustBeSubmittedBefore() {
        return couponMustBeSubmittedBefore;
    }

    @JsonProperty("matches")
    public List<Match> getMatches() {
        return matches;
    }
}
