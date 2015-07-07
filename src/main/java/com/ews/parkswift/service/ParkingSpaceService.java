package com.ews.parkswift.service;

import javax.inject.Inject;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;





import com.ews.parkswift.domain.AvailableParking;
import com.ews.parkswift.domain.AvailableParkingRepeatOn;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.domain.ParkingSpacePriceEntry;
import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import com.ews.parkswift.repository.AvailableParkingRepeatOnRepository;
import com.ews.parkswift.repository.AvailableParkingRepository;
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
    private AvailableParkingRepository availableParkingRepository; 
    @Inject
    private AvailableParkingRepeatOnRepository availableParkingRepeatOnRepository;

	public void save(ParkingSpace parkingSpace) {
		parkingSpaceRepository.save(parkingSpace);
		if(!parkingSpace.getParkingSpaceVehicleTypes().isEmpty())
			parkingSpace.getParkingSpaceVehicleTypes().forEach((ParkingSpaceVehicleType e)->{e.setParkingSpace(parkingSpace);});
			parkingSpaceVehicleTypeRepository.save(parkingSpace.getParkingSpaceVehicleTypes());
		if(!parkingSpace.getParkingSpacePriceEntrys().isEmpty())
			parkingSpace.getParkingSpacePriceEntrys().forEach((ParkingSpacePriceEntry e)->{e.setParkingSpace(parkingSpace);});
			parkingSpacePriceEntryRepository.save(parkingSpace.getParkingSpacePriceEntrys());
		if(!parkingSpace.getAvailableParkings().isEmpty()){
			parkingSpace.getAvailableParkings().forEach((AvailableParking e)->{e.setParkingSpace(parkingSpace);});
			availableParkingRepository.save(parkingSpace.getAvailableParkings());
			for(AvailableParking e:parkingSpace.getAvailableParkings()){
				e.getAvailableParkingRepeatOns().forEach((AvailableParkingRepeatOn ero)->{ero.setAvailableParking(e);});
				availableParkingRepeatOnRepository.save(e.getAvailableParkingRepeatOns());
			}
			
				
			
		}
		
		
	}
	

}
