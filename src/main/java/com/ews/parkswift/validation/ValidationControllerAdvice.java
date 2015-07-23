package com.ews.parkswift.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ValidationControllerAdvice {

	@Inject
	private MessageSource messageSource;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public List<FieldErrorMessageDTO> processMethodArgumentValidationError(MethodArgumentNotValidException ex) {
		List<FieldErrorMessageDTO> lst_messageDTOs = new ArrayList<>();
		BindingResult result = ex.getBindingResult();
		for(FieldError error:result.getFieldErrors())
			lst_messageDTOs.add(processFieldError(error));
		return lst_messageDTOs;
	}
	

	private FieldErrorMessageDTO processFieldError(FieldError error) {
		FieldErrorMessageDTO message = null;
		if (error != null) {
			Locale currentLocale = LocaleContextHolder.getLocale();
			String msg = messageSource.getMessage(error.getCode(),
					null, error.getDefaultMessage(), currentLocale);
			message = new FieldErrorMessageDTO(error.getField(), msg);
		}
		return message;
	}
}
