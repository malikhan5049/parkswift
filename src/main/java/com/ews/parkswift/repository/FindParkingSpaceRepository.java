package com.ews.parkswift.repository;

import java.util.List;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.web.rest.dto.parking.FindParkingsDTO;


/**
 * Spring Data JPA repository for the ParkingSpace entity.
 */
public interface FindParkingSpaceRepository {
	
	public static final String HAVERSINE_PART = "( case :distanceUnit when 'mile' then 3959 else 6371 end * acos( cos( radians(:lattitude) ) * cos( radians( pl.lattitude ) ) * cos( radians( pl.longitude ) "
			+ "- radians(:longitude) ) + sin( radians(:lattitude) ) * sin( radians( pl.lattitude ) ) ) )";
	
	public static String FIND_PARKING_QUERY_SELECT = "SELECT distinct pl,ps,avs,"+HAVERSINE_PART+" as distance ";
	public static String FIND_PARKING_QUERY_FROM = "FROM ParkingLocation pl "
			+ "join pl.parkingSpaceEntitys ps "
			+ "join ps.availabilitySchedules avs ";
	public static String FIND_PARKING_QUERY_WHERE = "where pl.active = true "
			+ "and "+HAVERSINE_PART+" < :distance "
			+ "and avs.startDate <= :startDate and avs.endDate >=:endDate "
			+ "and avs.startTime <=:startTime and avs.endTime>=:endTime "
//			+ "and ps.numberOfSpaces > "
			+ "and "
				+ "(ps.numberOfSpaces - (select count(bps.parkingSpace) from BookedParkingSpace bps "
				+ "join bps.customerBooking cb "
				+ "join cb.bookingSchedule bs "
				+ "where ps.id = bps.parkingSpace.id and "
				+ "cb.status = 'BOOKED' and "  
				+ "((bs.startDateTime <= :startDateTime and :startDateTime < bs.endDateTime) or " 
				+ "(bs.startDateTime < :endDateTime and :endDateTime <= bs.endDateTime) or "
				+ "(:startDateTime <= bs.startDateTime and bs.startDateTime < :endDateTime) or " 
		        + "(:startDateTime < bs.endDateTime and bs.endDateTime <= :endDateTime)))) > 0";
	public static String FIND_PARKING_QUERY = FIND_PARKING_QUERY_SELECT
			+ FIND_PARKING_QUERY_FROM
			+ FIND_PARKING_QUERY_WHERE
			;

	public List<Object[]> findParkingSpaces(FindParkingsDTO parkingLocation);
	
}
