package org.jon.ivmark.footballcoupons.application.game.service;

import org.jon.ivmark.footballcoupons.application.game.domain.GameRepository;
import org.jon.ivmark.footballcoupons.application.game.domain.GameService;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Coupon;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Game;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

// TODO: Avoid synchronization
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public synchronized void addCoupon(GameId gameId, Coupon coupon) {
        Game game = gameRepository.getGame(gameId);
        game.addCoupon(coupon);
        gameRepository.saveGame(game);
    }
}