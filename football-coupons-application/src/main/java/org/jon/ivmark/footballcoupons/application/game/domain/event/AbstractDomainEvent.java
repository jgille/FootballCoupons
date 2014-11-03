package org.jon.ivmark.footballcoupons.application.game.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.ValueObject;

public class AbstractDomainEvent<T extends DomainEventType> extends ValueObject implements DomainEvent<T> {

    private final String aggregateId;
    private final T eventType;
    private final long timestamp;

    public AbstractDomainEvent(String aggregateId, T eventType, long timestamp) {
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    @Override
    @JsonProperty("aggregate_id")
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    @JsonProperty("timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    @JsonProperty("event_type")
    public T getEventType() {
        return eventType;
    }

}
