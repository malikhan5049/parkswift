package com.ews.parkswift.web.filter;

import com.fasterxml.jackson.databind.JsonNode;



public class RestResponse {
	private String status;
	private String failureMessage;
	private Object content;
	private String path;
	
	
	public RestResponse(String status, String failureMessage, Object content, String path) {
		super();
		this.status = status;
		this.failureMessage = failureMessage;
		this.content = content;
		this.path = path;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getFailureMessage() {
		return failureMessage;
	}
	public Object getContent() {
		return content;
	}
	
	public String getPath() {
		return path;
	}
	@Override
	public String toString() {
		return "RestResponse [status=" + status + ", message=" + failureMessage
				+ ", content=" + content + "]";
	}
	
	
}
