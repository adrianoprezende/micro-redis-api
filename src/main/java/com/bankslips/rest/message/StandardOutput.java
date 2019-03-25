package com.bankslips.rest.message;

import java.io.Serializable;
import java.util.List;

/**
 * The Class StandardOutput.
 */
public class StandardOutput implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8502163836140990033L;

	/** The http status code and output message. */
	private Integer statusCode;
	
	/** The message. */
	private String message;

	/**
	 * Stores the error's list. It's required when status = FAIL
	 */
	private List<MessageInfo> listError;

	/**
	 * Response Object. Could store objects from any type according to it's service
	 * specifics necessity.
	 */
	private Object object;

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Integer getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatusCode(Integer status) {
		this.statusCode = status;
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

	/**
	 * Gets the list error.
	 *
	 * @return the list error
	 */
	public List<MessageInfo> getListError() {
		return listError;
	}

	/**
	 * Sets the list error.
	 *
	 * @param listError the new list error
	 */
	public void setListError(List<MessageInfo> listError) {
		this.listError = listError;
	}

	/**
	 * Gets the object.
	 *
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * Sets the object.
	 *
	 * @param object the new object
	 */
	public void setObject(Object object) {
		this.object = object;
	}


}
