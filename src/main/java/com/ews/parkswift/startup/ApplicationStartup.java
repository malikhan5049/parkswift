package com.ews.parkswift.startup;

import java.io.File;
import java.net.Inet4Address;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.LookupEntry;
import com.ews.parkswift.domain.LookupHeader;
import com.ews.parkswift.repository.LookupHeaderRepository;


public class ApplicationStartup implements InitializingBean,EnvironmentAware{
	private RelaxedPropertyResolver serverPropertyResolver;
	private Environment environment;
	
	@Inject
	private LookupHeaderRepository lookupHeaderRepository;
	
	private List<LookupHeader> lst_lookupHeader = Collections.emptyList();
	public static Map<LookupHeaderCode, Set<LookupEntry>> map_lookupHeader = new HashMap<>();
	public static enum LookupHeaderCode{LOC_TYPE, LOC_FACILITY, PS_PP, PS_VT, REP_BASIS, REP_AFTR_EVRY, REP_BY, REP_END_BASIS, WEEKDAYS}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		populateLookupHeader();
		String applicationContextName = environment.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)?"/parkswift":"";
		Constants.LOCATION_IMAGES_PARENT_FOLDER_PATH = environment.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT) || 
				environment.acceptsProfiles(Constants.SPRING_PROFILE_FAST)?System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"webapp"+File.separator+"assets":
					System.getProperty("catalina.base")+File.separator+"webapps"+
				File.separator+applicationContextName+File.separator+Constants.LOCATION_IMAGES_PARENT_FOLDER_NAME;
		Constants.LOCATION_IMAGES_FOLDER_PATH = Constants.LOCATION_IMAGES_PARENT_FOLDER_PATH+File.separator+Constants.LOCATION_IMAGES_FOLDER_NAME;
		FileUtils.forceMkdir(new File(Constants.LOCATION_IMAGES_FOLDER_PATH));
		
		Constants.LOCATION_IMAGES_FOLDER_URL = Inet4Address.getLocalHost().getHostAddress()+":"+serverPropertyResolver.getProperty("port")+
				applicationContextName+"/"+Constants.LOCATION_IMAGES_PARENT_FOLDER_NAME+"/"+Constants.LOCATION_IMAGES_FOLDER_NAME;
	}

	private void populateLookupHeader() {
		lst_lookupHeader = lookupHeaderRepository.findAll();
		for(LookupHeader e:lst_lookupHeader){
			map_lookupHeader.put(LookupHeaderCode.valueOf(e.getCode()), e.getLookupEntrys());
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
		serverPropertyResolver =  new RelaxedPropertyResolver(environment, "server.");
		
	}
}
