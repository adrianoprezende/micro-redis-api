package com.bankslips.exception;

import org.springframework.http.HttpStatus;

/**
 * The Class AbstractException.
 */
public abstract class AbstractException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -237868328020577671L;
	
	/** The http status. */
	protected HttpStatus httpStatus;
	
	/**
	 * Instantiates a new abstract exception.
	 *
	 * @param status the status
	 * @param message the message
	 */
	public AbstractException(final HttpStatus status, final String message) {
		super(message);
		this.httpStatus = status;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public HttpStatus getStatus() {
		return httpStatus;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(HttpStatus status) {
		this.httpStatus = status;
	}
}
