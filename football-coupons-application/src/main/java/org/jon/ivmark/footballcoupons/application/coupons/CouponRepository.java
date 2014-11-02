package org.jon.ivmark.footballcoupons.application.coupons;

import org.jon.ivmark.footballcoupons.application.coupons.domain.aggregates.Coupon;

public interface CouponRepository {

    void createCoupon(String gameId, Coupon coupon);
}
