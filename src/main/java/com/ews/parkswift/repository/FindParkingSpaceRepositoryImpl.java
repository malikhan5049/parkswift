package com.ews.parkswift.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Repository;

import com.ews.parkswift.web.rest.dto.parking.FindParkingsDTO;

@Repository
public class FindParkingSpaceRepositoryImpl implements
		FindParkingSpaceRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
		
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findParkingSpaces(FindParkingsDTO findParkingDTO) {
		
		StringBuffer findParkingQueryString = buildFindParkingQueryString(findParkingDTO);
		Query query = this.entityManager.createQuery(findParkingQueryString.toString());
		populateQueryParameters(findParkingDTO, query);
		return query.getResultList();
	}

	private void populateQueryParameters(FindParkingsDTO findParkingDTO,
			Query query) {
		query.setParameter("distanceUnit", findParkingDTO.getDistanceUnit());
		query.setParameter("distance", findParkingDTO.getDistance());
		query.setParameter("longitude", findParkingDTO.getLongitude());
		query.setParameter("lattitude", findParkingDTO.getLattitude());
		query.setParameter("startDate", findParkingDTO.getAvailabilitySchedule().getStartDate());
		query.setParameter("endDate", findParkingDTO.getAvailabilitySchedule().getEndDate());
		query.setParameter("startTime", findParkingDTO.getAvailabilitySchedule().getStartTime());
		query.setParameter("endTime", findParkingDTO.getAvailabilitySchedule().getEndTime());
		
		LocalDate startDate = findParkingDTO.getAvailabilitySchedule().getStartDate();
		LocalDate endDate = findParkingDTO.getAvailabilitySchedule().getEndDate();
		LocalTime startTime = findParkingDTO.getAvailabilitySchedule().getStartTime();
		LocalTime endTime = findParkingDTO.getAvailabilitySchedule().getEndTime();
		
		query.setParameter("startDateTime", new DateTime(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth(), startTime.getHourOfDay(), startTime.getMinuteOfHour()));
		query.setParameter("endDateTime", new DateTime(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), endTime.getHourOfDay(), endTime.getMinuteOfHour()));
	}

	private StringBuffer buildFindParkingQueryString(
			FindParkingsDTO findParkingDTO) {
		StringBuffer findParkingQueryString = new StringBuffer(FIND_PARKING_QUERY_SELECT);
		findParkingQueryString.append(FIND_PARKING_QUERY_FROM);
		addOptionalJoinClauses(findParkingDTO, findParkingQueryString);
		findParkingQueryString.append(FIND_PARKING_QUERY_WHERE);
		addOptionalAndClauses(findParkingDTO, findParkingQueryString);
		return findParkingQueryString;
	}
	
		

	private void addOptionalAndClauses(FindParkingsDTO findParkingDTO,
			StringBuffer findParkingQueryString) {
		if(!findParkingDTO.getParkingLocationFacilitys().isEmpty()){
			addOptionalFacilitiesAndClause(findParkingDTO,
					findParkingQueryString);
		}
		
		if(!findParkingDTO.getParkingSpaceVehicleTypes().isEmpty()){
			addOptionalCarTypesAndClause(findParkingDTO, findParkingQueryString);
		}
		
		if(!findParkingDTO.getParkingSpacePriceEntrys().isEmpty()){
			addOptionalPricePlansAndClause(findParkingDTO,
					findParkingQueryString);
		}
	}

	private void addOptionalPricePlansAndClause(FindParkingsDTO findParkingDTO,
			StringBuffer findParkingQueryString) {
		findParkingQueryString.append("and (select count(id) from ps.parkingSpacePriceEntrys pspe where pspe.type in( ");
		findParkingDTO.getParkingSpacePriceEntrys().forEach((e)->{
			commaSeparatedValue(findParkingQueryString, e.getType());
		});
		deleteLastComma(findParkingQueryString);
		findParkingQueryString.append(") and pspe.price in( ");
		findParkingDTO.getParkingSpacePriceEntrys().forEach((e)->{
			commaSeparatedValue(findParkingQueryString, e.getPrice());
		});
		deleteLastComma(findParkingQueryString);
		findParkingQueryString.append(")) = ");
		findParkingQueryString.append(findParkingDTO.getParkingSpacePriceEntrys().size());
	}

	private StringBuffer deleteLastComma(StringBuffer findParkingQueryString) {
		return findParkingQueryString.replace(StringUtils.lastIndexOf(findParkingQueryString, ","), findParkingQueryString.length(), "");
	}

	private void commaSeparatedValue(StringBuffer findParkingQueryString,
			Object value) {
		findParkingQueryString.append("'");
		findParkingQueryString.append(value);
		findParkingQueryString.append("'");
		findParkingQueryString.append(",");
	}

	private void addOptionalCarTypesAndClause(FindParkingsDTO findParkingDTO,
			StringBuffer findParkingQueryString) {
		findParkingQueryString.append("and pswt.type in( ");
		findParkingDTO.getParkingSpaceVehicleTypes().forEach((e)->{
			commaSeparatedValue(findParkingQueryString, e.getType());
		});
		deleteLastComma(findParkingQueryString);
		findParkingQueryString.append(") ");
	}

	private void addOptionalFacilitiesAndClause(FindParkingsDTO findParkingDTO,
			StringBuffer findParkingQueryString) {
		findParkingQueryString.append("and plf.facility in( ");
		findParkingDTO.getParkingLocationFacilitys().forEach((e)->{
			commaSeparatedValue(findParkingQueryString, e.getFacility());
		});
		deleteLastComma(findParkingQueryString);
		findParkingQueryString.append(") ");
	}

	private void addOptionalJoinClauses(FindParkingsDTO findParkingDTO,
			StringBuffer findParkingQueryString) {
		addOptionalJoinClause(!findParkingDTO.getParkingLocationFacilitys().isEmpty(), "join pl.parkingLocationFacilitys plf ", findParkingQueryString);
		addOptionalJoinClause(!findParkingDTO.getParkingSpaceVehicleTypes().isEmpty(), "join ps.parkingSpaceVehicleTypes pswt ", findParkingQueryString);
	}
	
	private void addOptionalJoinClause(boolean add, String clauseToAdd,StringBuffer findParkingQueryString){
		if(add)
			findParkingQueryString.append(clauseToAdd);
	}

}
