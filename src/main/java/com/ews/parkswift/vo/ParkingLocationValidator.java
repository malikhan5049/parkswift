package com.ews.parkswift.vo;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.repository.PaypallAccountRepository;


public class ParkingLocationValidator implements Validator{
	
	PaypallAccountRepository paypallAccountRepository;
	
	public ParkingLocationValidator(PaypallAccountRepository paypallAccountRepository){
		this.paypallAccountRepository = paypallAccountRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		if(ParkingLocationImage.class.equals(clazz)){
			return ParkingLocationImage.class.equals(clazz);
		}
		return ParkingLocation.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(target instanceof ParkingLocation){
		ParkingLocation parkingLocation = (ParkingLocation) target;
		int index=0;
//		for(ParkingLocationImage e:parkingLocation.getParkingLocationImages().toArray(new ParkingLocationImage[]{})){
//			if(e.getId() == null && e.getImage() == null)
//				errors.rejectValue("parkingLocationImages["+index+++"].image", null,"may not be null");
//		}
//		if(parkingLocation.getPaypallAccount().getId() == null){
//			if(paypallAccountRepository.findOneByEmail(parkingLocation.getPaypallAccount().getEmail()).isPresent()){
//				errors.rejectValue("paypallAccount.email", null,"already exist in DB choose another one");
//			}
//		}else{
//			paypallAccountRepository.findOneById(parkingLocation.getPaypallAccount().getId()).ifPresent((e)->{
//				if(!e.getEmail().equals(parkingLocation.getPaypallAccount().getEmail()) 
//						&& paypallAccountRepository.findOneByEmail(parkingLocation.getPaypallAccount().getEmail()).isPresent()) 
//					errors.rejectValue("paypallAccount.email", null,"already exist in DB choose another one");
//			});
//		}
	}	
	}
	
}