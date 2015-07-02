package com.ews.parkswift.startup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.beans.factory.InitializingBean;

import com.ews.parkswift.domain.LookupEntry;
import com.ews.parkswift.domain.LookupHeader;
import com.ews.parkswift.repository.LookupHeaderRepository;


public class ApplicationStartup implements InitializingBean{
	
	@Inject
	private LookupHeaderRepository lookupHeaderRepository;
	
	private List<LookupHeader> lst_lookupHeader = Collections.emptyList();
	public static Map<LookupHeaderCode, Set<LookupEntry>> map_lookupHeader = new HashMap<>();
	public static enum LookupHeaderCode{LOC_TYPE, LOC_FACILITY, PS_PP, PS_VT}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		lst_lookupHeader = lookupHeaderRepository.findAll();
		for(LookupHeader e:lst_lookupHeader){
			map_lookupHeader.put(LookupHeaderCode.valueOf(e.getCode()), e.getLookupEntrys());
		}
	}
}
