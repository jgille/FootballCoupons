package org.jon.ivmark.footballcoupons.application.game.infrastructure;

import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Game;
import org.jon.ivmark.footballcoupons.application.game.domain.exception.NoSuchGameException;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryGameRepositoryTest {

    @Mock
    private Game game;

    @Test
    public void saveAndGetGame() {
        InMemoryGameRepository repository = new InMemoryGameRepository();
        GameId gameId = new GameId("game1");
        when(game.getGameId()).thenReturn(gameId);

        repository.saveGame(game);
        Game repositoryGame = repository.getGame(gameId);

        assertThat(repositoryGame, is(game));
    }

    @Test(expected = NoSuchGameException.class)
    public void getNonExistingGame() {
        InMemoryGameRepository repository = new InMemoryGameRepository();
        GameId gameId = new GameId("game1");

        repository.getGame(gameId);
    }
}
