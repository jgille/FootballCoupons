package org.jon.ivmark.footballcoupons.application.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.contrib.json.JsonFormatter;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.AbstractAppenderFactory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonTypeName("json")
public class JsonAppenderFactory extends AbstractAppenderFactory {

    protected static final String USERNAME = "username";
    private static final String UTC = "UTC";

    @NotNull
    private String currentLogFilename;

    @NotNull
    private String archivedLogFilenamePattern;

    @Min(1)
    private int archivedFileCount = 5;

    @NotNull
    private String timestampFormat;

    @JsonProperty
    public void setCurrentLogFilename(String currentLogFilename) {
        this.currentLogFilename = currentLogFilename;
    }

    @JsonProperty
    public void setArchivedLogFilenamePattern(String archivedLogFilenamePattern) {
        this.archivedLogFilenamePattern = archivedLogFilenamePattern;
    }

    @JsonProperty
    public void setArchivedFileCount(int archivedFileCount) {
        this.archivedFileCount = archivedFileCount;
    }

    @JsonProperty
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    @Override
    public Appender<ILoggingEvent> build(LoggerContext context, String s, Layout<ILoggingEvent> layout) {
        FileAppender<ILoggingEvent> appender = createAppender(context);
        appender.setName("json-appender");
        JsonFormatter jsonFormatter = createJsonFormatter();
        JsonLayout jsonLayout = createJsonLayout(jsonFormatter);
        appender.setLayout(jsonLayout);
        appender.setContext(context);
        appender.setEncoder(createJsonEncoder(context, jsonLayout));
        appender.setFile(currentLogFilename);
        appender.setPrudent(false);
        appender.setAppend(true);
        addThresholdFilter(appender, threshold);
        appender.start();
        jsonLayout.start();
        return wrapAsync(appender);
    }

    private Encoder<ILoggingEvent> createJsonEncoder(LoggerContext context, JsonLayout jsonLayout) {
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setLayout(jsonLayout);
        encoder.setContext(context);
        return encoder;
    }

    private FileAppender<ILoggingEvent> createAppender(LoggerContext context) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        DefaultTimeBasedFileNamingAndTriggeringPolicy<ILoggingEvent> triggeringPolicy =
                new DefaultTimeBasedFileNamingAndTriggeringPolicy<>();
        triggeringPolicy.setContext(context);

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setContext(context);
        rollingPolicy.setFileNamePattern(archivedLogFilenamePattern);
        rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(triggeringPolicy);
        triggeringPolicy.setTimeBasedRollingPolicy(rollingPolicy);
        rollingPolicy.setMaxHistory(archivedFileCount);

        appender.setRollingPolicy(rollingPolicy);
        appender.setTriggeringPolicy(triggeringPolicy);

        rollingPolicy.setParent(appender);
        rollingPolicy.start();
        return appender;
    }

    private JsonLayout createJsonLayout(JsonFormatter jsonFormatter) {
        JsonLayout jsonLayout = new UsernamePrependingJsonLayout();
        jsonLayout.setJsonFormatter(jsonFormatter);
        jsonLayout.setTimestampFormat(timestampFormat);
        jsonLayout.setTimestampFormatTimezoneId(UTC);
        jsonLayout.setIncludeMDC(false);
        jsonLayout.setIncludeContextName(false);
        jsonLayout.setAppendLineSeparator(true);
        return jsonLayout;
    }

    private JsonFormatter createJsonFormatter() {
        return new JacksonJsonFormatter();
    }

    static class UsernamePrependingJsonLayout extends JsonLayout {
        @Override
        protected Map toJsonMap(ILoggingEvent event) {
            Map<String, Object> map = new LinkedHashMap<>();
            Map<String, String> mdc = event.getMDCPropertyMap();
            if (mdc.containsKey(USERNAME)) {
                map.put(USERNAME, mdc.get(USERNAME));
            }
            map.putAll(super.toJsonMap(event));
            return map;
        }
    }

}
