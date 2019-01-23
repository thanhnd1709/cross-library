package com.crossover.techtrial.exceptions;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Component
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	public static final String DATE_FORMAT_ERROR_MESSAGE = "Please enter correct Date Time format";
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOG.error("Bad request: Method argument not valid exception.", ex);
		List<FieldError> errors = ex.getBindingResult().getFieldErrors();
		List<String> message = new ArrayList<>();
		for (FieldError e : errors) {
			message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
		}
		String bodyOfResponse = message.toString();
		return handleExceptionInternal(ex, bodyOfResponse, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOG.error("Bad request: Message not readable.", ex);
		return handleExceptionInternal(ex, DATE_FORMAT_ERROR_MESSAGE, headers, status, request);
	}
	
	@ExceptionHandler(value = { DataIntegrityViolationException.class})
	public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
		LOG.error("Bad request: Violate data integrity constraint.", exception);
		AbstractMap.SimpleEntry<String, String> response = new AbstractMap.SimpleEntry<>("message",
				exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(value= {TransactionException.class}) 
	public ResponseEntity<Object> handleRideException(TransactionException exception) {
		// general exception
		LOG.error("Exception: Transaction exception ", exception);
		return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
	}
	/**
	 * Global Exception handler for all exceptions.
	 */
	@ExceptionHandler
	public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(Exception exception) {
		// general exception
		LOG.error("Exception: Unable to process this request. ", exception);
		AbstractMap.SimpleEntry<String, String> response = new AbstractMap.SimpleEntry<>("message",
				"Unable to process this request.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
