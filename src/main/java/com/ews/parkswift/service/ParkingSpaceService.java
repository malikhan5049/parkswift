package com.ews.parkswift.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.repository.ParkingLocationImageRepository;
import com.ews.parkswift.repository.ParkingSpacePriceEntryRepository;
import com.ews.parkswift.repository.ParkingSpaceRepository;
import com.ews.parkswift.repository.ParkingSpaceVehicleTypeRepository;

@Service
@Transactional
public class ParkingSpaceService {

    private final Logger log = LoggerFactory.getLogger(ParkingSpaceService.class);
    @Inject
    private ParkingSpaceRepository parkingSpaceRepository;
    @Inject
    private ParkingSpaceVehicleTypeRepository parkingSpaceVehicleTypeRepository;
    @Inject
    private ParkingSpacePriceEntryRepository parkingSpacePriceEntryRepository;
    @Inject
    private ParkingLocationImageRepository parkingSpaceImageRepository;

	public void save(ParkingSpace parkingSpace) {
		parkingSpaceRepository.save(parkingSpace);
		if(!parkingSpace.getParkingSpaceVehicleTypes().isEmpty())
			parkingSpaceVehicleTypeRepository.save(parkingSpace.getParkingSpaceVehicleTypes());
		if(!parkingSpace.getParkingSpacePriceEntrys().isEmpty())
			parkingSpacePriceEntryRepository.save(parkingSpace.getParkingSpacePriceEntrys());
		/*if(!parkingSpace.getParkingSpaceImages().isEmpty()){
			parkingSpace.getParkingSpaceImages().forEach(parkingSpaceImage->parkingSpaceImage.setParkingSpace(parkingSpace));
			parkingSpaceImageRepository.save(parkingSpace.getParkingSpaceImages());
		}*/
		
	}

}
