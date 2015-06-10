package com.ews.parkswift.service;

import javax.inject.Inject;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.repository.ParkingLocationRepository;

@Service
@Transactional
public class ParkingLocationServiceService {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationServiceService.class);
    
    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    
    public void save(ParkingLocation parkingLocation) throws ServiceException{
    	
    	parkingLocationRepository.save(parkingLocation);
    }

}
