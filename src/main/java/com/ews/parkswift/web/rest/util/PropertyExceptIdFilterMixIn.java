package com.ews.parkswift.web.rest.util;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter(JacksonUtils.INCLUDEONLYIDFIELDSFILTER_ID)
public class PropertyExceptIdFilterMixIn {

}
