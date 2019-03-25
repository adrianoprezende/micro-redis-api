package com.bankslips.rest.message;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

/**
 * The Class OutputStatus.
 */
public class OutputStatus implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7870629035770763630L;

	/** The http status. */
	private Integer httpStatus;
	
	/** The output message. */
	private String outputMessage;
	
	/**
	 * Instantiates a new output status.
	 *
	 * @param status the status
	 * @param outputMessage the output message
	 */
	public OutputStatus(HttpStatus status, String outputMessage) {
		this.httpStatus = status.value();
		this.outputMessage = outputMessage;
	}

	/**
	 * Gets the http status.
	 *
	 * @return the http status
	 */
	public Integer getHttpStatus() {
		return httpStatus;
	}

	/**
	 * Sets the http status.
	 *
	 * @param httpStatus the new http status
	 */
	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

	/**
	 * Gets the output message.
	 *
	 * @return the output message
	 */
	public String getOutputMessage() {
		return outputMessage;
	}

	/**
	 * Sets the output message.
	 *
	 * @param outputMessage the new output message
	 */
	public void setOutputMessage(String outputMessage) {
		this.outputMessage = outputMessage;
	}

}
