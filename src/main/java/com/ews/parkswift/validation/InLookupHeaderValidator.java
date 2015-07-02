package com.ews.parkswift.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ews.parkswift.startup.ApplicationStartup;

public class InLookupHeaderValidator implements ConstraintValidator<InLookupHeader, String>{
	
	InLookupHeader InLookupHeader;
	@Override
	public void initialize(InLookupHeader InLookupHeader) {
		this.InLookupHeader = InLookupHeader;
		
	}

	@Override
	public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
		if(ApplicationStartup.map_lookupHeader.get(InLookupHeader.code()).stream().anyMatch(LE->LE.getValue().equalsIgnoreCase(field)))
			return true;
		return false;
	}

}
