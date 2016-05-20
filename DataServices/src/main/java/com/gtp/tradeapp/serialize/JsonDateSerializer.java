package com.gtp.tradeapp.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonDateSerializer extends JsonSerializer<DateTime> {
    private final static DateTimeFormatter DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(DateTime date, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        String formattedDate = DATETIME_FORMAT.print(date);
        gen.writeString(formattedDate);
    }
}