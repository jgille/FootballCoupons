package org.jon.ivmark.footballcoupons.application.game.domain.event;

public interface DomainEvent<T extends DomainEventType> {

    String getAggregateId();

    long getTimestamp();

     T getEventType();
}
