package org.jon.ivmark.footballcoupons.application.game.domain.valueobjects;

public class MatchId extends ValueObject {

    private final CouponId couponId;

    // TODO: Might not be smart to use index. What if a match is deleted from a coupon?
    private final int matchIndex;

    public MatchId(CouponId couponId, int matchIndex) {
        this.couponId = couponId;
        this.matchIndex = matchIndex;
    }

    public CouponId getCouponId() {
        return couponId;
    }

    public int getMatchIndex() {
        return matchIndex;
    }
}
