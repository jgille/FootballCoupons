package org.jon.ivmark.footballcoupons.application.game.service;

import org.jon.ivmark.footballcoupons.application.game.domain.GameRepository;
import org.jon.ivmark.footballcoupons.application.game.domain.GameService;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Coupon;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Game;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

// TODO: Synchronization can be avoided if each game is handled by a dedicated thread
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public synchronized void createGame(GameId gameId, String gameName) {
        Game game = Game.createGame(gameId, gameName);
        gameRepository.saveGame(game);
    }

    @Override
    public synchronized void saveCoupon(GameId gameId, Coupon coupon) {
        Game game = gameRepository.getGame(gameId);
        game.saveCoupon(coupon);
        gameRepository.saveGame(game);
    }

}
