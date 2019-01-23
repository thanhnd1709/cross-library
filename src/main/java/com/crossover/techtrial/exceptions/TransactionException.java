package com.crossover.techtrial.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private HttpStatus status;

	public TransactionException() {
		super();
	}
	public TransactionException(String message) {
		super(message);
	}
	public TransactionException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

}
