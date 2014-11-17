package org.jon.ivmark.footballcoupons.application.game.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.commons.io.FileUtils;
import org.jon.ivmark.footballcoupons.application.game.domain.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class FileBasedEventLog<T extends DomainEvent> implements EventLog<T> {

    private final File directory;

    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

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

    @Override
    public void replayEvents(EventHandler<T> eventHandler) {
        File[] aggregateDirs = directory.listFiles();
        if (aggregateDirs == null) {
            return;
        }
        for (File aggregateDir : aggregateDirs) {
            replayEvents(aggregateDir, eventHandler);
        }
    }

    private void replayEvents(File aggregateDir, EventHandler<T> eventHandler) {
        File[] files = aggregateDir.listFiles();
        if (files == null) {
            return;
        }

        List<File> eventFiles = Arrays.asList(files);
        Collections.sort(eventFiles, new EventFileComparator());

        for (File eventFile : eventFiles) {
            replayEvent(eventFile, eventHandler);
        }
    }

    private void replayEvent(File eventFile, EventHandler<T> eventHandler) {
        try {
            Map map = objectMapper.readValue(eventFile, Map.class);
            String cls = (String) map.get("@class");
            Class<?> aClass = Class.forName(cls);
            Object event = objectMapper.convertValue(map, aClass);
            eventHandler.replayEvent(event);
        } catch (Exception e) {
            throw new EventLogException("Failed to read event", e);
        }
    }

    private void writeAndPublish(T event) {
        logger.info("Writing event of type {} for aggregate with id {}", event.getEventType().name(),
                    event.getAggregateId());
        try {
            FileUtils.writeStringToFile(eventFile(event), serialize(event));
        } catch (IOException e) {
            throw new EventLogException("Failed to write event: " + event, e);
        }
        logger.info("Event written successfully");
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
        return String.format("%d-%s.json", event.getTimestamp().toDate().getTime(),
                             event.getEventType().name());
    }

    static class EventFileComparator implements Comparator<File> {

        private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^(\\d+)-.*.json$");

        @Override
        public int compare(File f1, File f2) {
            String f1Name = f1.getName();
            String f2Name = f2.getName();
            return compareByTimeStamp(f1Name, f2Name);
        }

        private int compareByTimeStamp(String f1Name, String f2Name) {
            Matcher matcher1 = FILE_NAME_PATTERN.matcher(f1Name);
            Matcher matcher2 = FILE_NAME_PATTERN.matcher(f2Name);

            if (!(matcher1.find() && matcher2.find())) {
                throw new EventLogException("Illegal event log file name");
            }

            Long timestamp1 = Long.valueOf(matcher1.group(1));
            Long timestamp2 = Long.valueOf(matcher2.group(1));

            return timestamp1.compareTo(timestamp2);
        }
    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JodaModule());
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        FileBasedEventLog<GameEvent> eventLog = new FileBasedEventLog<>(new File("/tmp/data/games/events"),
                                                                        objectMapper);

        eventLog.replayEvents(new SimpleEventHandler());
    }
}
