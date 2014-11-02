package org.jon.ivmark.footballcoupons.application.game.infrastructure.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jon.ivmark.footballcoupons.application.game.domain.event.AbstractDomainEvent;
import org.jon.ivmark.footballcoupons.application.game.domain.event.DomainEventType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

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
        this.eventLog = new FileBasedEventLog<>(dir, objectMapper);
    }

    @Test
    public void writeSingleEvent() throws IOException {
        long timestamp = 1000L;
        TestEventType eventType = TestEventType.EVENT_TYPE_1;
        TestEvent event = new TestEvent(eventType, timestamp);

        eventLog.writeEvents(Collections.singletonList(event));

        File file = eventLog.eventFile(event);
        TestEvent savedEvent = objectMapper.readValue(file, TestEvent.class);
        assertThat(savedEvent, is(event));
    }

    private static class TestEvent extends AbstractDomainEvent<TestEventType> {

        private TestEvent() {

        }

        private TestEvent(TestEventType eventType, long timestamp) {
            this.eventType = eventType;
            this.timestamp = timestamp;
            this.aggregateId = "test";
        }

    }

    private static enum TestEventType implements DomainEventType {
        EVENT_TYPE_1
    }
}
