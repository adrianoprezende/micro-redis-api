package com.bankslips.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bankslips.rest.message.MessageInfo;
import com.bankslips.service.IServiceMessage;

/**
 * The Class ServiceMessageImpl.
 */
@Service
public class ServiceMessageImpl implements IServiceMessage {
	
	private static final String COLON = " : ";

	/** The Constant HYPHEN. */
	private static final String HYPHEN = "-";
	
	/** The message source. */
	@Autowired
    private MessageSource messageSource;

    private String get(String key) {
    	Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key,null,locale);
    }

	/* (non-Javadoc)
	 * @see com.bankslips.utils.IMessagesPropertiesUtil#getMessage(java.lang.String)
	 */
	@Override
	public String getMessage(String key) {
		return get(key);
	}
	
	/* (non-Javadoc)
	 * @see com.bankslips.utils.IMessagesUtil#getServiceMessage(java.lang.String)
	 */
	@Override
	public MessageInfo getServiceMessage(String key) {
		return new MessageInfo(key, get(key));
	}
	
	/* (non-Javadoc)
	 * @see com.bankslips.utils.IMessagesUtil#mergeMessages(java.util.List)
	 */
	@Override
	public List<MessageInfo> mergeMessages(final List<MessageInfo> messageList) {
		final Map<String, MessageInfo> errorMap = new HashMap<>();
		for (final MessageInfo serviceMessage : messageList) {
			String mergedKeyMessage = serviceMessage.getCode() + HYPHEN + serviceMessage.getMessage();
			if (!errorMap.containsKey(mergedKeyMessage)) {
				errorMap.put(mergedKeyMessage, serviceMessage);
			}
		}
		return new ArrayList<>(errorMap.values());
	}
	
	@Override
	public String getOutputMessageBy(final HttpStatus status) {
		StringBuilder strBuilder = new StringBuilder();
		if(status != null) {
			strBuilder
			.append(status.value())
			.append(COLON)
			.append(get(String.valueOf(status.value())));
		}
		return strBuilder.toString();
	}
}
