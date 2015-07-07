package com.ews.parkswift.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.EnumUtils;

public class ValidWeekDayValidator implements ConstraintValidator<ValidWeekDay, String>{
	private static enum WEEKDAY{SATURDAY,SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY};
	ValidWeekDay validWeekDay;
	@Override
	public void initialize(ValidWeekDay validWeekDay) {
		this.validWeekDay = validWeekDay;
		
	}

	@Override
	public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
		if(field == null || EnumUtils.isValidEnum(WEEKDAY.class, field))
			return true;
		return false;
	}

}
