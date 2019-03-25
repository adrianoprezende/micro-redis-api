package com.bankslips.exception;

import org.springframework.http.HttpStatus;

public class BankslipBadRequestException extends AbstractException {

	private static final long serialVersionUID = 4599976361861963146L;

	public BankslipBadRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}

}
