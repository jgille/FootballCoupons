package org.jon.ivmark.footballcoupons.application.game.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.ValueObject;

public class AbstractDomainEvent<T extends DomainEventType> extends ValueObject implements DomainEvent<T> {

    private final String aggregateId;
    private final T eventType;
    private final DateTime timestamp;

    public AbstractDomainEvent(String aggregateId, T eventType, DateTime timestamp) {
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
    public DateTime getTimestamp() {
        return timestamp;
    }

    @Override
    @JsonProperty("event_type")
    public T getEventType() {
        return eventType;
    }

}
