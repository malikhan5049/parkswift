package com.ews.parkswift.domain.util;

import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

import com.ews.parkswift.config.Constants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Custom Jackson deserializer for displaying Joda DateTime objects.
 */
public class CustomLocalDateTimeDeserializer extends
		JsonDeserializer<LocalDateTime> {

	private static DateTimeFormatter formatter = Constants.LOCALDATETIMEFORMATTER;

	@Override
	public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException {
		JsonToken t = jp.getCurrentToken();
		if (t == JsonToken.VALUE_STRING) {
			String str = jp.getText().trim();
			return formatter.parseLocalDateTime(str);
		}
		if (t == JsonToken.VALUE_NUMBER_INT) {
			return new LocalDateTime(jp.getLongValue());
		}
		throw ctxt.mappingException(handledType());
	}
}
