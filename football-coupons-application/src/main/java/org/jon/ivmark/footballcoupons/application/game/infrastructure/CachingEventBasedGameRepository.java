package org.jon.ivmark.footballcoupons.application.game.infrastructure;

import org.jon.ivmark.footballcoupons.application.game.domain.GameRepository;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Game;
import org.jon.ivmark.footballcoupons.application.game.domain.event.EventLog;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

import java.util.List;

public class CachingEventBasedGameRepository implements GameRepository {

    private final InMemoryGameRepository cache;

    private final EventLog<GameEvent> eventLog;

    public CachingEventBasedGameRepository(InMemoryGameRepository cache, EventLog<GameEvent> eventLog) {
        this.cache = cache;
        this.eventLog = eventLog;
    }

    @Override
    public Game getGame(GameId gameId) {
        return cache.getGame(gameId);
    }

    @Override
    public void saveGame(Game game) {
        List<GameEvent> uncommitedEvents = game.getUncommitedEvents();
        eventLog.writeEvents(uncommitedEvents);
        cache.saveGame(game);
    }

}
