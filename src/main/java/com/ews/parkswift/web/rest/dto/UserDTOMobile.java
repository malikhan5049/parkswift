package com.ews.parkswift.web.rest.dto;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTOMobile extends UserDTO{
	
	public UserDTOMobile(String firstName,
			String lastName, String email, List<String> roles) {
		super(null,null,firstName,lastName,email,null, roles);
	}
	@JsonIgnore
	@Override
	public String getLogin() {
		// TODO Auto-generated method stub
		return super.getLogin();
	}
	@JsonIgnore
	@Override
	public String getLangKey() {
		// TODO Auto-generated method stub
		return super.getLangKey();
	}
	
	@JsonIgnore
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return super.getPassword();
	}
	
	
	
}
