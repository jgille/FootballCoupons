package org.jon.ivmark.footballcoupons.application.game.domain.aggregates;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CouponTest {

    @Test
    public void maySubmitPredictionNow() {
        DateTime afterNow = LocalDateTime.now().toDateTime().plusDays(1);
        Coupon coupon = Coupon.newCoupon("name", afterNow, 10);
        assertThat(coupon.maySubmitPredictionNow(), is(true));
    }

    @Test
    public void mayNotSubmitPredictionNow() {
        DateTime afterNow = LocalDateTime.now().toDateTime().minusDays(1);
        Coupon coupon = Coupon.newCoupon("name", afterNow, 10);
        assertThat(coupon.maySubmitPredictionNow(), is(false));
    }

    @Test
    public void newCoupon() {
        String name = "test name";
        DateTime couponMustBeSubmittedBefore = LocalDateTime.now().toDateTime();
        int numberOfMatches = 13;
        Coupon coupon = Coupon.newCoupon(name, couponMustBeSubmittedBefore, numberOfMatches);

        assertThat(coupon.getCouponId(), notNullValue());
        assertThat(coupon.getCouponId().getValue(), notNullValue());
        assertThat(coupon.getCouponName(), is(name));
        assertThat(coupon.getCouponMustBeSubmittedBefore(), is(couponMustBeSubmittedBefore));
        assertThat(coupon.getMatches().size(), is(numberOfMatches));
    }
}
