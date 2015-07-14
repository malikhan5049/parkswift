package com.ews.parkswift.web.filter;




public class RestResponse {
	private String status;
	private String failureMessage;
	private Object failureContent;
	private Object content;
	private String path;
	
	
	public RestResponse(String status, String failureMessage, Object failureContent,Object content, String path) {
		super();
		this.status = status;
		this.failureMessage = failureMessage;
		this.failureContent = failureContent;
		this.content = content;
		this.path = path;
	}
	
	
	
	public Object getFailureContent() {
		return failureContent;
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
