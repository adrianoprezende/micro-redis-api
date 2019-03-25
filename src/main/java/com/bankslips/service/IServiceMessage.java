package com.bankslips.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bankslips.rest.message.MessageInfo;

@Component
public interface IServiceMessage {
	
	/**
	 * Gets the message.
	 *
	 * @param key the key
	 * @return the message
	 */
	String getMessage(String key);
	
	/**
	 * Gets the service message.
	 *
	 * @param key the key
	 * @return the service message
	 */
	MessageInfo getServiceMessage(String key);
	
	/**
	 * Merge messages.
	 *
	 * @param messageList the message list
	 * @return the list
	 */
	List<MessageInfo> mergeMessages(final List<MessageInfo> messageList);

	
	/**
	 * Gets the output message by.
	 *
	 * @param status the status
	 * @return the output message by
	 */
	String getOutputMessageBy(HttpStatus status);

}
