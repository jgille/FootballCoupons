package org.jon.ivmark.footballcoupons.application.game.domain.event;

import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.ValueObject;

public class AbstractDomainEvent<T extends DomainEventType> extends ValueObject implements DomainEvent<T> {

    public String aggregateId;
    public T eventType;
    public long timestamp;

    protected AbstractDomainEvent() {

    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public T getEventType() {
        return eventType;
    }

}
