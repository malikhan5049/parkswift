package com.ews.parkswift.domain.util;

import java.io.IOException;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

import com.ews.parkswift.config.Constants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * ISO 8601 date format
 * Jackson deserializer for displaying Joda LocalTime objects.
 */
public class CustomLocalTimeDeserializer extends JsonDeserializer<LocalTime> {
	
	private static DateTimeFormatter formatter = Constants.LOCALTIMEFORMATTER;
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
