package com.bankslips.rest.validator.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankslips.constants.MessageCodesConstants;
import com.bankslips.exception.BankslipBadRequestException;
import com.bankslips.exception.BankslipInvalidFieldsException;
import com.bankslips.rest.dto.BankSlipDTO;
import com.bankslips.rest.message.MessageInfo;
import com.bankslips.rest.validator.IBankslipValidator;
import com.bankslips.rest.validator.ValidationAbstract;
import com.bankslips.service.IServiceMessage;
import com.bankslips.utils.UUIDUtils;

@Service
public class BankslipValidatorImpl extends ValidationAbstract implements IBankslipValidator {
	
	@Autowired
	private IServiceMessage messageUtils;
	
	@Override
	public void validateFields(final BankSlipDTO request) throws BankslipInvalidFieldsException {
		List<MessageInfo> messages = new ArrayList<>();
		validateRequiredFields(request, messages, this.messageUtils);
		if (!messages.isEmpty()) {
			throw new BankslipInvalidFieldsException(messageUtils.mergeMessages(messages),
					this.messageUtils.getMessage(MessageCodesConstants.INVALID_BANKSLIP));
		}
	}
	
	@Override
	public void validateId(final String id) throws BankslipBadRequestException {
		if(!id.matches((UUIDUtils.REGEX_VALID_UUID))) {
			throw new BankslipBadRequestException(messageUtils.getMessage(MessageCodesConstants.INVALID_ID_PROVIDED));
		}
	}

}
