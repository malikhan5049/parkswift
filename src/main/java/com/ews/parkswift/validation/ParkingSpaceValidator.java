package com.ews.parkswift.validation;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.AvailabilitySchedule;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.repository.ParkingLocationRepository;


public class ParkingSpaceValidator implements Validator{
	
    private ParkingLocationRepository parkingLocationRepository;
	
	public ParkingSpaceValidator(ParkingLocationRepository parkingLocationRepository){
		this.parkingLocationRepository = parkingLocationRepository;
	}
    

	@Override
	public boolean supports(Class<?> clazz) {
		return ParkingSpace.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ParkingSpace parkingSpace = (ParkingSpace) target;
		if(Optional.ofNullable(parkingSpace.getNumberOfSpaces()).isPresent())
        	if(parkingSpace.getNumberOfSpaces() > Constants.BUSINESS_TYPE_RESIDENTIAL_MAX_SPACE_LIMIT && 
        			parkingLocationRepository.findOne(parkingSpace.getParkingLocation().getId()).getBussinessType().equalsIgnoreCase(Constants.BUSINESS_TYPE_RESIDENTIAL))
        		errors.rejectValue("numberOfSpaces", null,"Can not create parking spaces more than "+Constants.BUSINESS_TYPE_RESIDENTIAL_MAX_SPACE_LIMIT+" for parking type "+Constants.BUSINESS_TYPE_RESIDENTIAL);
		
		int index = 0;
		for(AvailabilitySchedule e: parkingSpace.getAvailabilitySchedules()){
			if(e.getRepeatBasis()!=null){
				if(e.getRepeatAfterEvery() == null){
					errors.rejectValue("availabilitySchedules["+index+"].repeatAfterEvery", null,"may not be null when repeatBasis is not null");
				}
				if(e.getRepeatBasis().equalsIgnoreCase(Constants.REPEAT_BASIS_WEEKLY)){
					if(e.getAvailabilityScheduleRepeatOns().isEmpty())
						errors.rejectValue("availabilitySchedules["+index+"].availabilityScheduleRepeatOns", null,"may not be empty when repeatBasis is 'Weekly'");
				}else if(e.getRepeatBasis().equalsIgnoreCase(Constants.REPEAT_BASIS_MONTHLY)){
					if(e.getRepeatBy() == null)
						errors.rejectValue("availabilitySchedules["+index+"].repeatBy", null,"may not be empty when repeatBasis is 'Monthly'");
				}
				
				if(!e.getAvailabilityScheduleRepeatOns().isEmpty() && !e.getRepeatBasis().equalsIgnoreCase(Constants.REPEAT_BASIS_WEEKLY))
					errors.rejectValue("availabilitySchedules["+index+"].availabilityScheduleRepeatOns", null,"should only be defined when repeatBasis is 'Weekly'");
				if(e.getRepeatBy() != null && !e.getRepeatBasis().equalsIgnoreCase(Constants.REPEAT_BASIS_MONTHLY))
					errors.rejectValue("availabilitySchedules["+index+"].repeatBy", null,"should only be defined when repeatBasis is 'Monthly'");
					
				
				if(e.getRepeatEndBasis() == null){
					errors.rejectValue("availabilitySchedules["+index+"].repeatEndBasis", null,"may not be null when repeatBasis is not null");
				}else if(e.getRepeatEndBasis().equalsIgnoreCase(Constants.REPEAT_END_BASIS_AFTER)){
					if(e.getRepeatEndOccurrences() == null)
						errors.rejectValue("availabilitySchedules["+index+"].repeatEndOccurrences", null,"may not be null when repeatEndBasis is 'After'");
				}else if(e.getRepeatEndBasis().equalsIgnoreCase(Constants.REPEAT_END_BASIS_ON)){
					if(e.getRepeatEndDate() == null)
						errors.rejectValue("availabilitySchedules["+index+"].repeatEndDate", null,"may not be null when repeatEndBasis is 'On'");
					else if(e.getRepeatEndDate().compareTo(e.getStartDate())<=0){
						errors.rejectValue("availabilitySchedules["+index+"].repeatEndDate", null,"repeatEndDate must be greater than startDate");
					}
				}
				
				if(e.getRepeatEndOccurrences()!=null && !Constants.REPEAT_END_BASIS_AFTER.equalsIgnoreCase(e.getRepeatEndBasis()))
					errors.rejectValue("availabilitySchedules["+index+"].repeatEndOccurrences", null,"should only be defined when repeatEndBasis is 'After'");
				if(e.getRepeatEndDate() != null && !Constants.REPEAT_END_BASIS_ON.equalsIgnoreCase(e.getRepeatEndBasis()))
					errors.rejectValue("availabilitySchedules["+index+"].repeatEndDate", null,"should not be defined when repeatEndBasis is 'On'");
					
			}
			index++;
		}
		
	}
	
}