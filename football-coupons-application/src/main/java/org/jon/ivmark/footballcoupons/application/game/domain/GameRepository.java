package org.jon.ivmark.footballcoupons.application.game.domain;

import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Game;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

public interface GameRepository {

    Game getGame(GameId gameId);

    void saveGame(Game game);
}
