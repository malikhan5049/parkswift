package com.ews.parkswift.service;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.repository.ParkingSpaceRepository;

@Service
@Transactional
public class ParkingSpaceService {

    private final Logger log = LoggerFactory.getLogger(ParkingSpaceService.class);
    @Inject
    private ParkingSpaceRepository parkingSpaceRepository;

	public void save(ParkingSpace parkingSpace) {
		if(Optional.ofNullable(parkingSpace.getNumberOfSpaces()).isPresent()){
			parkingSpace.setGroupRecord(true);
		}
		parkingSpaceRepository.save(parkingSpace);
	}
	

}
