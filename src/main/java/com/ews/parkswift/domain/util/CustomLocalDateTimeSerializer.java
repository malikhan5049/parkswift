package com.ews.parkswift.domain.util;

import java.io.IOException;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

import com.ews.parkswift.config.Constants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Custom Jackson serializer for displaying Joda DateTime objects.
 */
public class CustomLocalDateTimeSerializer extends
		JsonSerializer<LocalDateTime> {

	private static DateTimeFormatter formatter = Constants.LOCALDATETIMEFORMATTER;

	@Override
	public void serialize(LocalDateTime value, JsonGenerator generator,
			SerializerProvider serializerProvider) throws IOException {
		generator.writeString(formatter.print(value
				.toDateTime(DateTimeZone.UTC)));
	}

}
