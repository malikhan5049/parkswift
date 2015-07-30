package com.ews.parkswift.validation;

import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.joda.time.LocalDate;

import com.ews.parkswift.config.Constants;

public class DateInEraValidator implements ConstraintValidator<DateInEra, Object>{
	
	DateInEra dateInEra;
	@Override
	public void initialize(DateInEra InLookupHeader) {
		this.dateInEra = InLookupHeader;
		
	}

	@Override
	public boolean isValid(Object field, ConstraintValidatorContext constraintValidatorContext) {
		if(field == null)
			return true;
		final LocalDate date = LocalDate.parse(field.toString(), Constants.LOCALDATEFORMATTER);
		return Stream.of(dateInEra.era()).anyMatch((era)->{
			if(era.equals(Era.PAST) && date.compareTo(LocalDate.now())<0)
				return true;
			if(era.equals(Era.PRESENT) && date.compareTo(LocalDate.now()) == 0)
				return true;
			if(era.equals(Era.FUTURE) && date.compareTo(LocalDate.now())>0)
				return true;
			return false;
		});
	}

}
