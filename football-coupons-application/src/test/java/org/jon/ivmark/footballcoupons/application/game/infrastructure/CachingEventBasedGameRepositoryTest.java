package org.jon.ivmark.footballcoupons.application.game.infrastructure;

import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Game;
import org.jon.ivmark.footballcoupons.application.game.domain.event.EventLog;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CachingEventBasedGameRepositoryTest {

    private CachingEventBasedGameRepository repository;

    @Mock
    private InMemoryGameRepository cache;

    @Mock
    private EventLog<GameEvent> eventLog;

    @Mock
    private Game game;

    @Before
    public void init() {
        this.repository = new CachingEventBasedGameRepository(cache, eventLog);
    }

    @Test
    public void saveGame() {
        GameEvent event1 = mock(GameEvent.class);
        GameEvent event2 = mock(GameEvent.class);
        List<GameEvent> events = Arrays.asList(event1, event2);
        when(game.getUncommitedEvents()).thenReturn(events);

        repository.saveGame(game);

        verify(eventLog).writeEvents(events);
        verify(cache).saveGame(game);
    }
}
