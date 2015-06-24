package com.ews.parkswift.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.repository.ParkingLocationContactInfoRepository;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.web.rest.ParkingLocationResource;

@Service
@Transactional
public class ParkingLocationContactInfoService {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationContactInfoService.class);
    
    @Inject
    private ParkingLocationContactInfoRepository parkingLocationContactInfoRepository;
    
    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    
    public void delete(Long id){
    	ParkingLocation parkingLocation = parkingLocationRepository.findOneByParkingLocationContactInfoId(id);
    	if(parkingLocation!=null)
    		parkingLocation.setParkingLocationContactInfo(null);
    	
    	parkingLocationContactInfoRepository.delete(id);
    }

}
