package org.jon.ivmark.footballcoupons.application.game.domain.event;

public interface EventHandler<T extends DomainEvent> {
    void replayEvent(Object event);
}
