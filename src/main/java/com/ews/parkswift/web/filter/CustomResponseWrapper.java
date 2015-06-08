package com.ews.parkswift.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.http.HttpStatus;

public class CustomResponseWrapper extends HttpServletResponseWrapper {

	ByteArrayOutputStream output;
	FilterServletOutputStream filterOutput;
	HttpStatus status = HttpStatus.OK;

	public CustomResponseWrapper(HttpServletResponse response) {
		super(response);
		output = new ByteArrayOutputStream();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (filterOutput == null) {
			filterOutput = new FilterServletOutputStream(output);
		}
		return filterOutput;
	}

	public byte[] getDataStream() {
		return output.toByteArray();
	}

}
