package com.ews.parkswift.web.rest.dto;


public class PaypallAccountDTO {

	private String authToken;
	private String paypallEmail;
	private String name;
	private String middleName;
	private String accessToken;
	private String accountType;
	private String payKey;
	private String authorization_id;
	private String amount;
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAuthorization_id() {
		return authorization_id;
	}
	public void setAuthorization_id(String authorization_id) {
		this.authorization_id = authorization_id;
	}
	public String getPayKey() {
		return payKey;
	}
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
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
