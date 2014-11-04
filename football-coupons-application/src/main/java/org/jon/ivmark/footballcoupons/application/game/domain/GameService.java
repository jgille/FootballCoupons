package org.jon.ivmark.footballcoupons.application.game.domain;

import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Coupon;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

public interface GameService {
    void saveCoupon(GameId gameId, Coupon coupon);

    void createGame(GameId newGameId, String gameName);
}
