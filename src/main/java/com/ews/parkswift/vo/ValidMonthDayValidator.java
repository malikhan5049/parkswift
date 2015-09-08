package com.ews.parkswift.vo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.EnumUtils;
import org.joda.time.DateTimeConstants;

import com.ews.parkswift.startup.ApplicationStartup;

public class ValidMonthDayValidator implements ConstraintValidator<ValidMonthDay, Integer>{
	ValidMonthDay validMonthDay;
	@Override
	public void initialize(ValidMonthDay validMonthDay) {
		this.validMonthDay = validMonthDay;
		
	}

	@Override
	public boolean isValid(Integer field, ConstraintValidatorContext constraintValidatorContext) {
		if(field == null || (field >=0 && field <28))
			return true;
		return false;
	}

}
