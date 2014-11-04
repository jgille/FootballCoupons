package org.jon.ivmark.footballcoupons.application.game.domain.event;

import org.joda.time.DateTime;

public interface DomainEvent<T extends DomainEventType> {

    String getAggregateId();

    DateTime getTimestamp();

     T getEventType();
}
