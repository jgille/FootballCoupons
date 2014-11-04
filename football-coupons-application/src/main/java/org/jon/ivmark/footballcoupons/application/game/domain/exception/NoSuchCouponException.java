package org.jon.ivmark.footballcoupons.application.game.domain.exception;

import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.CouponId;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

public class NoSuchCouponException extends IllegalArgumentException {
    public NoSuchCouponException(GameId gameId, CouponId couponId) {
        super(String.format("No coupon found with id: %s for game with id: %s", couponId.getValue(), gameId.getValue()));
    }
}
