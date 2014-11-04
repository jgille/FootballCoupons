package org.jon.ivmark.footballcoupons.application.game.infrastructure.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jon.ivmark.footballcoupons.application.game.domain.event.AbstractDomainEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.DomainEventType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileBasedEventLogTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File dir;
    private ObjectMapper objectMapper;
    private FileBasedEventLog<TestEvent> eventLog;

    @Before
    public void init() throws IOException {
        this.dir = temporaryFolder.newFolder();
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JodaModule());
        this.eventLog = new FileBasedEventLog<>(dir, objectMapper);
    }

    @Test
    public void writeSingleEvent() throws IOException {
        String aggregateId = "test";
        DateTime timestamp = DateTime.now(DateTimeZone.UTC);
        TestEventType eventType = TestEventType.EVENT_TYPE_1;
        TestEvent event = new TestEvent(aggregateId, eventType, timestamp);

        eventLog.writeEvents(Collections.singletonList(event));

        File file = eventLog.eventFile(event);
        TestEvent savedEvent = objectMapper.readValue(file, TestEvent.class);
        assertThat(savedEvent, is(event));
    }

    private static class TestEvent extends AbstractDomainEvent<TestEventType> {

        private TestEvent(@JsonProperty("aggregate_id") String aggregateId,
                          @JsonProperty("event_type") TestEventType eventType,
                          @JsonProperty("timestamp") DateTime timestamp) {
            super(aggregateId, eventType, timestamp);
        }

    }

    private static enum TestEventType implements DomainEventType {
        EVENT_TYPE_1
    }
}
