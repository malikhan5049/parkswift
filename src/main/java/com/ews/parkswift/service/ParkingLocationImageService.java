package com.ews.parkswift.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.repository.ParkingLocationImageRepository;
import com.ews.parkswift.repository.ParkingLocationRepository;

@Service
@Transactional
public class ParkingLocationImageService {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationImageService.class);
    @Inject
    private ParkingLocationImageRepository parkingLocationImageRepository;
    @Inject
    private ParkingLocationRepository parkingLocationRepository;
	public List<byte[]> findAllByParkingLocation(
			Long parkingLocationId) {
		return parkingLocationImageRepository.findAllByParkingLocation(parkingLocationRepository.findOne(parkingLocationId)).
				stream().map((ParkingLocationImage parkingLocationImage)->{return parkingLocationImage.getImage();}).collect(Collectors.toList());
	}
    

}
