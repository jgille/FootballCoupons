package org.jon.ivmark.footballcoupons.application.game.infrastructure.event;

import org.jon.ivmark.footballcoupons.application.game.domain.event.CouponSavedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameCreatedEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.ReflectionsEventHandler;


public class SimpleEventHandler extends ReflectionsEventHandler<GameEvent> {

    @Replayble
    void gameCreated(GameCreatedEvent event) {
        System.out.println("Game created: " + event);
    }

    @Replayble
    void couponSaved(CouponSavedEvent event) {
        System.out.println("Coupon saved: " + event);
    }
}
