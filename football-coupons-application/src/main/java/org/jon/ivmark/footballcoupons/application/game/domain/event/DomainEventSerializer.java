package org.jon.ivmark.footballcoupons.application.game.domain.event;

public interface DomainEventSerializer<T extends DomainEvent> {

    String serialize(T event);

}
