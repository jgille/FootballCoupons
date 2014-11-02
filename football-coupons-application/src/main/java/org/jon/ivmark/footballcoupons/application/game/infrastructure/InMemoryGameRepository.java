package org.jon.ivmark.footballcoupons.application.game.infrastructure;

import org.jon.ivmark.footballcoupons.application.game.domain.GameRepository;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Game;
import org.jon.ivmark.footballcoupons.application.game.domain.exception.NoSuchGameException;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InMemoryGameRepository implements GameRepository {

    private final Map<GameId, Game> games = Collections.synchronizedMap(new HashMap<GameId, Game>());

    public InMemoryGameRepository() {
    }

    @Override
    public Game getGame(GameId gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new NoSuchGameException(gameId);
        }
        return game;
    }

    @Override
    public void saveGame(Game game) {
        games.put(game.getGameId(), game);
    }
}
