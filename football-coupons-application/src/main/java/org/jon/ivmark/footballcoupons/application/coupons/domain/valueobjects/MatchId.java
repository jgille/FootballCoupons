package org.jon.ivmark.footballcoupons.application.coupons.domain.valueobjects;

public class MatchId extends ValueObject {

    private final CouponId couponId;
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
