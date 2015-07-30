package com.ews.parkswift.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.AvailabilitySchedule;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.repository.FindParkingSpaceRepository;
import com.ews.parkswift.web.rest.dto.parking.AvailableParkingDTO;
import com.ews.parkswift.web.rest.dto.parking.FindParkingsDTO;

@Service
@Transactional
public class FindParkingSpaceService {

    private final Logger log = LoggerFactory.getLogger(FindParkingSpaceService.class);
    

    @Inject
    private FindParkingSpaceRepository findParkingSpaceRepository;


	public List<AvailableParkingDTO> findParkingSpaces(
			FindParkingsDTO findAvailableParkingsDTO) {
		List<Object[]> availableParkings = findParkingSpaceRepository.findParkingSpaces(findAvailableParkingsDTO);
		
		final List<AvailableParkingDTO> availableParkingsDTO = populateAvailableParkingDTOWithResult(
				findAvailableParkingsDTO, availableParkings);
		return availableParkingsDTO;
	}


	private List<AvailableParkingDTO> populateAvailableParkingDTOWithResult(
			FindParkingsDTO findAvailableParkingsDTO,
			List<Object[]> availableParkings) {
		final List<AvailableParkingDTO> availableParkingsDTO = new ArrayList<>();
		availableParkings.forEach((Object[] objectArray)->{
			ParkingLocation parkingLocation = (ParkingLocation) objectArray[0];
			ParkingSpace parkingSpace = (ParkingSpace) objectArray[1];
			AvailabilitySchedule availabilitySchedule = (AvailabilitySchedule) objectArray[2];
			Double distance = (Double) objectArray[3];
			
			AvailableParkingDTO availableParkingDTO = new AvailableParkingDTO();
        	availableParkingDTO.setBussinessType(parkingLocation.getBussinessType());
        	availableParkingDTO.setAddressLine1(parkingLocation.getAddressLine1());
        	availableParkingDTO.setAddressLine2(parkingLocation.getAddressLine2());
        	availableParkingDTO.setCity(parkingLocation.getCity());
        	availableParkingDTO.setState(parkingLocation.getState());
        	availableParkingDTO.setZipCode(parkingLocation.getZipCode());
        	availableParkingDTO.setCountry(parkingLocation.getCountry());
        	availableParkingDTO.setLongitude(parkingLocation.getLongitude());
        	availableParkingDTO.setLattitude(parkingLocation.getLattitude());
        	availableParkingDTO.setDistance(distance);
        	availableParkingDTO.setDistanceUnit(findAvailableParkingsDTO.getDistanceUnit());
        	availableParkingDTO.setParkingSpaceId(parkingSpace.getId());
        	availableParkingDTO.setNick(parkingSpace.getNick());
        	availableParkingDTO.setAvailabilityScheduleId(availabilitySchedule.getId());
        	availableParkingDTO.setParkingSpacePriceEntrys(parkingSpace.getParkingSpacePriceEntrys());
        	availableParkingsDTO.add(availableParkingDTO);
		});
		return availableParkingsDTO;
	}
	
	public static void main(String[] args) {
		FindParkingsDTO findAvailableParkingsDTO = new FindParkingsDTO(){{setAvailabilitySchedule(new AvailabilitySchedule(){{
			setStartDate(LocalDate.parse("2015-07-29", Constants.LOCALDATEFORMATTER));
			setEndDate(LocalDate.parse("2015-07-30", Constants.LOCALDATEFORMATTER));
			setStartTime(LocalTime.parse("04:42 PM", Constants.LOCALTIMEFORMATTER));
			setEndTime(LocalTime.parse("09:30 PM", Constants.LOCALTIMEFORMATTER));
		}});}};
		
		DateTime startDateTime = findAvailableParkingsDTO.getAvailabilitySchedule().getStartDate().toDateTime(findAvailableParkingsDTO.getAvailabilitySchedule().getStartTime());
		DateTime endDateTime = findAvailableParkingsDTO.getAvailabilitySchedule().getEndDate().toDateTime(findAvailableParkingsDTO.getAvailabilitySchedule().getEndTime());
		
		Period period = new Period(startDateTime, endDateTime);
		System.out.println(period.getYears());
		System.out.println(period.getMonths());
		System.out.println(period.getDays());
		System.out.println(period.getHours());
	}
	

}
