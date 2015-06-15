package com.ews.parkswift.service;

import javax.inject.Inject;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.repository.ParkingLocationContactInfoRepository;
import com.ews.parkswift.repository.ParkingLocationFacilityRepository;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.repository.PaypallAccountRepository;

@Service
@Transactional
public class ParkingLocationService {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationService.class);
    
    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    @Inject
    private ParkingLocationContactInfoRepository parkingLocationContactInfoRepository;
    @Inject
    private PaypallAccountRepository paypallAccountRepository;
    @Inject
    private ParkingLocationFacilityRepository parkingLocationFacilityRepository;
    
    
    
    public void save(ParkingLocation parkingLocation) throws ServiceException{
    	parkingLocationRepository.save(parkingLocation);
    	if(parkingLocation.getParkingLocationContactInfo()!=null)
    		parkingLocationContactInfoRepository.save(parkingLocation.getParkingLocationContactInfo());
    	if(parkingLocation.getPaypallAccount()!=null)
    		paypallAccountRepository.save(parkingLocation.getPaypallAccount());
    	if(!parkingLocation.getParkingLocationFacilitys().isEmpty()){
    		for(ParkingLocationFacility e : parkingLocation.getParkingLocationFacilitys()){
    			e.setParkingLocation(parkingLocation);
    		}
    		parkingLocationFacilityRepository.save(parkingLocation.getParkingLocationFacilitys());
    	}
    	
    }



	public ParkingLocation findOne(Long id) {
		ParkingLocation parkingLocation = parkingLocationRepository.findOne(id);
		parkingLocation.getParkingLocationFacilitys().size(); 
		return parkingLocation;
	}



	public void delete(Long id) {
		ParkingLocation parkingLocation = findOne(id);
		parkingLocationContactInfoRepository.delete(parkingLocation.getParkingLocationContactInfo());
		parkingLocationFacilityRepository.delete(parkingLocation.getParkingLocationFacilitys());
		parkingLocationRepository.delete(id);
		
	}

}
