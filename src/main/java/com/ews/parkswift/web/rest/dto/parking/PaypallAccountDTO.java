package com.ews.parkswift.web.rest.dto.parking;


public class PaypallAccountDTO {

	private String authToken;
	private String paypallEmail;
	private String name;
	private String middleName;
	private String accessToken;
	private String accountType;
	
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getPaypallEmail() {
		return paypallEmail;
	}
	public void setPaypallEmail(String paypallEmail) {
		this.paypallEmail = paypallEmail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	
}
