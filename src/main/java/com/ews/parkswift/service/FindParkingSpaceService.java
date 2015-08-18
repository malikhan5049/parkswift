package com.ews.parkswift.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.AvailabilitySchedule;
import com.ews.parkswift.domain.CostingInputVO;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.domain.ParkingSpacePriceEntry;
import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import com.ews.parkswift.domain.PriceHit;
import com.ews.parkswift.domain.PricePlan;
import com.ews.parkswift.domain.TimeInterval;
import com.ews.parkswift.repository.FindParkingSpaceRepository;
import com.ews.parkswift.web.rest.dto.parking.AvailableParkingDTO;
import com.ews.parkswift.web.rest.dto.parking.FindParkingsDTO;


@Service
@Transactional
public class FindParkingSpaceService {

    private final Logger log = LoggerFactory.getLogger(FindParkingSpaceService.class);
    

    @Inject
    private FindParkingSpaceRepository findParkingSpaceRepository;
    
    private CostingService costingService = new CostingService();

	public List<AvailableParkingDTO> findParkingSpaces(
			FindParkingsDTO findAvailableParkingsDTO) {
		List<Object[]> availableParkings = findParkingSpaceRepository.findParkingSpaces(findAvailableParkingsDTO);
		
		final List<AvailableParkingDTO> availableParkingsDTO = populateAvailableParkingDTOWithResult(
				findAvailableParkingsDTO, availableParkings);
		return availableParkingsDTO;
	}

	private String mapPricePlanTypeToName(String type) {
		if ("Hourly (Day)".equals(type))
			return PricePlan.DAYHOUR.name();
		else if ("Hourly (Night)".equals(type))
			return PricePlan.NIGHTHOUR.name();
		else if ("12 Hour (Day)".equals(type))
			return PricePlan._12HOURSDAY.name();
		else if ("12 Hour (Night)".equals(type))
			return PricePlan._12HOURSNIGHT.name();
		else if ("Daily".equals(type))
			return PricePlan.FULLDAY.name();
		else if ("Monthly".equals(type))
			return PricePlan.FULLMONTH.name();
		else if ("Weekly".equals(type))
			return PricePlan.FULLWEEK.name();
		else
			return null;
	}
	
	private String mapNameToPricePlanType(String name) {
		if (PricePlan.DAYHOUR.name().equals(name))
			return "Hourly (Day)";
		else if (PricePlan.NIGHTHOUR.name().equals(name))
			return "Hourly (Night)";
		else if (PricePlan._12HOURSDAY.name().equals(name))
			return "12 Hour (Day)";
		else if (PricePlan._12HOURSNIGHT.name().equals(name))
			return "12 Hour (Night)";
		else if (PricePlan.FULLDAY.name().equals(name))
			return "Daily";
		else if (PricePlan.FULLMONTH.name().equals(name))
			return "Monthly";
		else if (PricePlan.FULLWEEK.name().equals(name))
			return "Weekly";
		else
			return null;
	}

	private List<AvailableParkingDTO> populateAvailableParkingDTOWithResult(
			FindParkingsDTO findAvailableParkingsDTO,
			List<Object[]> availableParkings) {
		final List<AvailableParkingDTO> availableParkingsDTO = new ArrayList<>();
		availableParkings.forEach((Object[] objectArray)->{
			ParkingLocation parkingLocation = (ParkingLocation) objectArray[0];
			ParkingSpace parkingSpace = (ParkingSpace) objectArray[1];
			
			ParkingSpacePriceEntry parkingSpacePriceEntry;
			Map<String, BigDecimal> ratesMap = new HashMap<String, BigDecimal>();
			Iterator<ParkingSpacePriceEntry> parkingSpacePriceEntrys = parkingSpace.getParkingSpacePriceEntrys().iterator();
			while (parkingSpacePriceEntrys.hasNext()) {
				parkingSpacePriceEntry = parkingSpacePriceEntrys.next();
				ratesMap.put(mapPricePlanTypeToName(parkingSpacePriceEntry.getType()), parkingSpacePriceEntry.getPrice());
			}
			
			CostingInputVO costingInputVO = new CostingInputVO(findAvailableParkingsDTO.getAvailabilitySchedule().getStartDate(), 
					findAvailableParkingsDTO.getAvailabilitySchedule().getEndDate(), 
					findAvailableParkingsDTO.getAvailabilitySchedule().getStartTime(), 
					findAvailableParkingsDTO.getAvailabilitySchedule().getEndTime(), ratesMap);
			
			List<PriceHit> priceHits = costingService.performCosting(costingInputVO);
			
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
        	
        	double totalCost = 0;
        	for (PriceHit priceHit : priceHits) {
        		totalCost += priceHit.getCost().doubleValue();
        		availableParkingDTO.getPriceHits().add(priceHit);
        	}
        	availableParkingDTO.setCost(new BigDecimal(totalCost));
        	
        	for (ParkingLocationImage plImage : parkingLocation.getParkingLocationImages())
        		availableParkingDTO.getParkingLocationImages().add(plImage.getURL());
        	
        	for (ParkingLocationFacility plFacility : parkingLocation.getParkingLocationFacilitys())
        		availableParkingDTO.getParkingLocationFacilitys().add(plFacility.getFacility());
        	
        	for (ParkingSpaceVehicleType psVehicleType : parkingSpace.getParkingSpaceVehicleTypes())
        		availableParkingDTO.getParkingSpaceVehicleTypes().add(psVehicleType.getType());
        	
        	availableParkingsDTO.add(availableParkingDTO);
		});
		return availableParkingsDTO;
	}
	
	public static void main(String[] args) {
		/*FindParkingsDTO findAvailableParkingsDTO = new FindParkingsDTO(){{setAvailabilitySchedule(new AvailabilitySchedule(){{
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
		System.out.println(period.getHours());*/
		
		/*int divident = 59;
		int divider = 30;
		System.out.println(divident/divider);*/
		
		/*Interval interval = new Interval(LocalTime.parse("8:00 AM", Constants.LOCALTIMEFORMATTER).toDateTimeToday(), LocalTime.parse("7:00 AM", Constants.LOCALTIMEFORMATTER).toDateTimeToday().plusDays(1));
		System.out.println(interval);
		System.out.println(interval.contains(LocalTime.parse("8:00 PM", Constants.LOCALTIMEFORMATTER).toDateTimeToday()));*/
		
		/*Period timePeriod = new Period(LocalTime.parse("01:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("03:00 AM", Constants.LOCALTIMEFORMATTER));
		System.out.println(timePeriod.getHours());*/
		System.out.println(TimeInterval.DAY.contains(LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER)));
		System.out.println(TimeInterval.DAY.contains(LocalTime.parse("07:00 PM", Constants.LOCALTIMEFORMATTER)));
		System.out.println(TimeInterval.DAY.contains(LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER)));
		
		System.out.println(TimeInterval.NIGHT.contains(LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER)));
		System.out.println(TimeInterval.NIGHT.contains(LocalTime.parse("07:00 AM", Constants.LOCALTIMEFORMATTER)));
		System.out.println(TimeInterval.NIGHT.contains(LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER)));
	}
	

}
