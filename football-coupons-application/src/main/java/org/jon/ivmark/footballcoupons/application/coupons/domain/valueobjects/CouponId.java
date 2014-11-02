package org.jon.ivmark.footballcoupons.application.coupons.domain.valueobjects;

import java.util.UUID;

public class CouponId extends Id<String> {
    public CouponId(String id) {
        super(id);
    }

    public static CouponId randomCouponId() {
        return new CouponId(UUID.randomUUID().toString());
    }
}
