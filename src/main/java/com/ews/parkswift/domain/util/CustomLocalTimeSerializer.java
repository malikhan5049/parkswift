package com.ews.parkswift.domain.util;

import java.io.IOException;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

import com.ews.parkswift.config.Constants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomLocalTimeSerializer extends JsonSerializer<LocalTime> {

    private static DateTimeFormatter formatter =Constants.LOCALTIMEFORMATTER;

    @Override
    public void serialize(LocalTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(formatter.print(value));
    }
    
    /*public static void main(String[] args) {
    	System.out.println(formatter.parseLocalTime("01:39:17 PM"));
    	System.out.println(formatter.print(formatter.parseLocalTime("01:39:17 PM")));
    	
	}*/
}
