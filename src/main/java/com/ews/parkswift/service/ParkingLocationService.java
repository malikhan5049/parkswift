package com.ews.parkswift.service;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.domain.PaypallAccount;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.ParkingLocationContactInfoRepository;
import com.ews.parkswift.repository.ParkingLocationFacilityRepository;
import com.ews.parkswift.repository.ParkingLocationImageRepository;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.repository.PaypallAccountRepository;
import com.ews.parkswift.security.SecurityUtils;

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
    @Inject
    private ParkingLocationImageRepository parkingLocationImageRepository;
    
    @Inject
    private UserService userService;
    
    
    
    public void save(ParkingLocation parkingLocation) throws ServiceException{
    	User user = userService.getUser();
    	parkingLocation.setUser(user);
    	parkingLocationRepository.save(parkingLocation);
    	if(parkingLocation.getParkingLocationContactInfo()!=null)
    		parkingLocationContactInfoRepository.save(parkingLocation.getParkingLocationContactInfo());
    	if(parkingLocation.getPaypallAccount()!=null){
    		PaypallAccount payPallAccount = parkingLocation.getPaypallAccount();
	    	parkingLocation.getPaypallAccount().setUser(user);
    		paypallAccountRepository.save(payPallAccount);
    		if(payPallAccount.getIsDefault()){
    			paypallAccountRepository.findAllForCurrentUser().stream().filter(pa->!pa.getEmail().equals(payPallAccount.getEmail())).
    				collect(Collectors.toList()).stream().forEach(pa->pa.setIsDefault(false));
    			
    		}
    		
    	}
    	if(!parkingLocation.getParkingLocationFacilitys().isEmpty()){
    		for(ParkingLocationFacility e : parkingLocation.getParkingLocationFacilitys()){
    			e.setParkingLocation(parkingLocation);
    		}
    		parkingLocationFacilityRepository.save(parkingLocation.getParkingLocationFacilitys());
    	}
    	if(!parkingLocation.getParkingLocationImages().isEmpty()){
    		for(ParkingLocationImage e: parkingLocation.getParkingLocationImages())
    			e.setParkingLocation(parkingLocation);
    		parkingLocationImageRepository.save(parkingLocation.getParkingLocationImages());
    	}
    }



	public void delete(Long id) {
		ParkingLocation parkingLocation = parkingLocationRepository.findOne(id);
		if(parkingLocation.getParkingLocationContactInfo()!=null)
			parkingLocationContactInfoRepository.delete(parkingLocation.getParkingLocationContactInfo());
		if(!parkingLocation.getParkingLocationFacilitys().isEmpty())
			parkingLocationFacilityRepository.delete(parkingLocation.getParkingLocationFacilitys());
		if(!parkingLocation.getParkingLocationImages().isEmpty())
			parkingLocationImageRepository.delete(parkingLocation.getParkingLocationImages());
		parkingLocationRepository.delete(id);
		
	}

}
