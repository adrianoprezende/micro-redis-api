package com.bankslips.exception;

import org.springframework.http.HttpStatus;

/**
 * The Class BankslipNotFoundException.
 */
public class BankslipNotFoundException extends AbstractException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5100735385298545807L;

	/**
	 * Instantiates a new bankslip not found exception.
	 *
	 * @param message the message
	 */
	public BankslipNotFoundException(final String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

}
