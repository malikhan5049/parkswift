package com.ews.parkswift.web.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class RestResponse {
	private String status;
	private String message;
	private String content;
	
	
	public RestResponse() {
		super();
	}
	public RestResponse(String status, String message, String content) {
		super();
		this.status = status;
		this.message = message;
		this.content = content;
	}
	
	public String getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	public String getContent() {
		return content;
	}
	@Override
	public String toString() {
		return "RestResponse [status=" + status + ", message=" + message
				+ ", content=" + content + "]";
	}
	
	
}
