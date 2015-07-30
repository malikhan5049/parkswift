package com.ews.parkswift.validation;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ews.parkswift.domain.AvailabilitySchedule;
import com.ews.parkswift.web.rest.dto.parking.FindParkingsDTO;


public class FindParkingsDTOValidator implements Validator{
	
	
	public FindParkingsDTOValidator(){
	}
    

	@Override
	public boolean supports(Class<?> clazz) {
		return FindParkingsDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		FindParkingsDTO findAvailableParkingsDTO = (FindParkingsDTO) target;
		AvailabilitySchedule availabilitySchedule = findAvailableParkingsDTO.getAvailabilitySchedule();
		if(availabilitySchedule!=null){
			if(availabilitySchedule.getEndDate()!=null && availabilitySchedule.getStartDate()!=null && 
					availabilitySchedule.getEndDate().compareTo(availabilitySchedule.getStartDate())<0)
				errors.rejectValue("availabilitySchedule.endDate", null,"should not be earlier than startDate");
			if(availabilitySchedule.getStartDate()!=null && availabilitySchedule.getStartTime()!=null &&
					availabilitySchedule.getStartDate().equals(LocalDate.now()) && availabilitySchedule.getStartTime().compareTo(LocalTime.now()) <=0 )
				errors.rejectValue("availabilitySchedule.startTime", null,"should not be equal/earlier than currentTime");
			if(availabilitySchedule.getEndTime()!=null && availabilitySchedule.getStartTime()!=null &&
					availabilitySchedule.getEndTime().compareTo(availabilitySchedule.getStartTime())<0)
				errors.rejectValue("availabilitySchedule.endTime", null,"should not be earlier than startTime");
		}
		
	}
	
}