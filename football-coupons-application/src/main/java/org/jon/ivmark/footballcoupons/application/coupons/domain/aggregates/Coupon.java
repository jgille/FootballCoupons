package org.jon.ivmark.footballcoupons.application.coupons.domain.aggregates;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.jon.ivmark.footballcoupons.application.coupons.domain.valueobjects.CouponId;
import org.jon.ivmark.footballcoupons.application.coupons.domain.valueobjects.MatchId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class Coupon extends AggregateRoot<CouponId> {

    private final String couponName;

    private final DateTime couponMustBeSubmittedBefore;

    private final List<Match> matches;

    public Coupon(CouponId couponId, String couponName, DateTime couponMustBeSubmittedBefore, List<Match> matches) {
        super(couponId);
        this.couponName = couponName;
        this.couponMustBeSubmittedBefore = couponMustBeSubmittedBefore;
        this.matches = new ArrayList<>(matches);
    }

    public CouponId getCouponId() {
        return getId();
    }

    public String getCouponName() {
        return couponName;
    }

    public DateTime getCouponMustBeSubmittedBefore() {
        return couponMustBeSubmittedBefore;
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    public boolean maySubmitPredictionNow() {
        return couponMustBeSubmittedBefore.isAfterNow();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    public static Coupon newCoupon(String couponName, DateTime couponMustBeSubmittedBefore, int numberOfMatches) {
        CouponId couponId = CouponId.randomCouponId();
        return new Coupon(couponId, couponName, couponMustBeSubmittedBefore, createMatches(couponId, numberOfMatches));
    }

    private static List<Match> createMatches(CouponId couponId, int numberOfMatches) {
        List<Match> matches = new ArrayList<>(numberOfMatches);
        for (int matchIndex = 0; matchIndex < numberOfMatches; matchIndex++) {
            matches.add(Match.newMatch(new MatchId(couponId, matchIndex)));
        }
        return matches;
    }
}
