package com.ews.parkswift.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomReponseForMobileFilter extends GenericFilterBean {
	
	final ObjectMapper mapper;
	
	public CustomReponseForMobileFilter() {
		mapper =  new ObjectMapper();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if(httpRequest.getHeader("User-Agent")!=null && httpRequest.getHeader("User-Agent").indexOf("Mobile") != -1){
			CustomResponseWrapper responseWrapper = new CustomResponseWrapper(
					(HttpServletResponse) response);
	
			chain.doFilter(request, responseWrapper);
			String responseContent = new String(responseWrapper.getDataStream());
			HttpStatus httpStatus = HttpStatus.valueOf(responseWrapper.getStatus());
			String status = httpStatus.is2xxSuccessful()?"true":"false";
			String message = "";
			if(httpStatus.is4xxClientError() || httpStatus.is5xxServerError()){
				message = responseContent;
				responseContent = "";
			}
	
			RestResponse fullResponse = new RestResponse(status, message,
					responseContent);
			response.setContentLength(-1); // will limit the response to 20 characters if not set to -1
			response.setContentType("application/json; charset=UTF-8");
			mapper.writeValue(response.getOutputStream(), fullResponse);
		}else
			chain.doFilter(request, response);
	}


}
