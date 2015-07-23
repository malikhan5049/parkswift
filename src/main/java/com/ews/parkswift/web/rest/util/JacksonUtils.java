package com.ews.parkswift.web.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class JacksonUtils {
	public static final String INCLUDEONLYIDFIELDSFILTER_ID = "include only id fields";
	private static ObjectMapper mapperForFilterAllFieldsExceptId = new ObjectMapper();
	
	static{
		mapperForFilterAllFieldsExceptId.addMixInAnnotations(Object.class, PropertyExceptIdFilterMixIn.class);
	}
	
	
	public static String writeValueAsStringWithPropertyExceptIdFilterMixIn(Object pojo, String... includeFieldNames) throws JsonProcessingException{
		FilterProvider filters = new SimpleFilterProvider()  
        .addFilter(INCLUDEONLYIDFIELDSFILTER_ID,   
            SimpleBeanPropertyFilter.filterOutAllExcept( includeFieldNames));
		return mapperForFilterAllFieldsExceptId.writer(filters).writeValueAsString(pojo);
		
	}

}
