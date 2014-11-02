package org.jon.ivmark.footballcoupons.application.game.domain.valueobjects;

import java.util.UUID;

public class CouponId extends AggregateId {
    public CouponId(String id) {
        super(id);
    }

    public static CouponId randomCouponId() {
        return new CouponId(UUID.randomUUID().toString());
    }
}
