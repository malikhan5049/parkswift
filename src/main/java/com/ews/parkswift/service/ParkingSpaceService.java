package com.ews.parkswift.service;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.repository.ParkingSpaceRepository;

@Service
@Transactional
public class ParkingSpaceService {

    private final Logger log = LoggerFactory.getLogger(ParkingSpaceService.class);
    @Inject
    private ParkingSpaceRepository parkingSpaceRepository;
    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    
    
	public void save(ParkingSpace parkingSpace) {
		if(Optional.ofNullable(parkingSpace.getNumberOfSpaces()).isPresent()){
			parkingSpace.setGroupRecord(true);
		}
		ParkingLocation parkingLocation = parkingLocationRepository.findOne(parkingSpace.getParkingLocation().getId());
		if(null!=parkingLocation){
			parkingSpace.setParkingLocation(parkingLocation);
		}
		parkingSpaceRepository.save(parkingSpace);
		parkingLocation.getParkingSpaceEntitys().add(parkingSpace);
		parkingLocationRepository.save(parkingLocation);
	}


}
