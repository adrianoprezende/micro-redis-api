package com.bankslips.rest.message;

import java.io.Serializable;

/**
 * The Class Message Info.
 */
public class MessageInfo implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7925867155471906826L;

	/** The id. */
	private String code;

	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new message info.
	 */
	public MessageInfo() {
	}

	/**
	 * Instantiates a new service message.
	 *
	 * @param code the id
	 * @param message the message
	 */
	public MessageInfo(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}


	/**
	 * Sets the code.
	 *
	 * @param code the new id
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
