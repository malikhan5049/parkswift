package com.ews.parkswift.validation;

public class FieldErrorMessageDTO {
	private String field;
	private String message;

	public FieldErrorMessageDTO() {
		super();
	}

	public FieldErrorMessageDTO(String field, String message) {
		super();
		this.message = message;
		this.field = field;
	}
	
	

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
