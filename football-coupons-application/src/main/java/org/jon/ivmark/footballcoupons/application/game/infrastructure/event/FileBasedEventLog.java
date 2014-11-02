package org.jon.ivmark.footballcoupons.application.game.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.jon.ivmark.footballcoupons.application.game.domain.event.DomainEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.EventLog;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileBasedEventLog<T extends  DomainEvent> implements EventLog<T> {

    private final File directory;

    private final ObjectMapper objectMapper;

    public FileBasedEventLog(File directory, ObjectMapper objectMapper) {
        this.directory = directory;
        this.objectMapper = objectMapper;
    }

    @Override
    public void writeEvents(List<T> events) {
        for (T event : events) {
            writeAndPublish(event);
        }
    }

    private void writeAndPublish(T event) {
        try {
            FileUtils.writeStringToFile(eventFile(event), serialize(event));
        } catch (IOException e) {
            throw new EventLogException("Failed to write event: " + event, e);
        }
    }

    File eventFile(T event) {
        return new File(aggregateDir(event), eventFileName(event));
    }

    private File aggregateDir(T event) {
        return new File(directory, event.getAggregateId());
    }

    private String serialize(T event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    private String eventFileName(T event) {
        return String.format("%d-%s.json", event.getTimestamp(), event.getEventType().name());
    }
}
