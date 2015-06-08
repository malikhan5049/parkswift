package com.ews.parkswift.web.filter;



public class RestResponse {
	private String status;
	private String failureMessage;
	private String content;
	private String path;
	
	
	public RestResponse() {
		super();
	}
	public RestResponse(String status, String failureMessage, String content, String path) {
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
	public String getContent() {
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
