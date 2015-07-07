package com.ews.parkswift.domain.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

/**
 * ISO 8601 date format
 * Jackson deserializer for displaying Joda LocalTime objects.
 */
public class CustomLocalTimeDeserializer extends JsonDeserializer<LocalTime> {
	
	private static DateTimeFormatter formatter = DateTimeFormat
            .forPattern("hh:mm:ss a");
    @Override
    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            return formatter.parseLocalTime(str);
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return new LocalTime(jp.getLongValue());
        }
        throw ctxt.mappingException(handledType());
    }
}
