package org.jon.ivmark.footballcoupons.application.coupons.infrastructure;

import org.jon.ivmark.footballcoupons.application.coupons.CouponRepository;
import org.jon.ivmark.footballcoupons.application.coupons.domain.aggregates.Coupon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryCouponRepository implements CouponRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void createCoupon(String gameId, Coupon coupon) {
        logger.info("Creating coupon with id {} and name {} for game {}",
                    coupon.getCouponId().getValue(), coupon.getCouponName(), gameId);
        // TODO: Implement
    }
}
