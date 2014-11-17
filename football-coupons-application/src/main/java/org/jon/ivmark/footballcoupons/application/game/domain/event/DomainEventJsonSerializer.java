package org.jon.ivmark.footballcoupons.application.game.domain.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.jon.ivmark.footballcoupons.application.game.domain.event.exception.EventException;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

public class DomainEventJsonSerializer<T extends DomainEvent> implements DomainEventSerializer<T> {
    private final ObjectMapper objectMapper;

    public DomainEventJsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(T event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new EventException("Failed to serialize event", e);
        }
    }

    public static void main(String[] args) {
        DomainEventJsonSerializer<GameEvent> serializer = new DomainEventJsonSerializer<>(new ObjectMapper());
        GameCreatedEvent event = new GameCreatedEvent(GameId.randomGameId(), DateTime.now(), "name");
        String serialized = serializer.serialize(event);
        System.out.println(serialized);
    }
}
