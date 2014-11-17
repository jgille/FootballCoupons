package org.jon.ivmark.footballcoupons.application.game.domain.event;

import java.util.List;

public interface EventLog<T extends  DomainEvent> {
    void writeEvents(List<T> events);

    void replayEvents(EventHandler<T> eventHandler);
}
