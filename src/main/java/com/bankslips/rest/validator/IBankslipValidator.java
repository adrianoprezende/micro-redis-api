package com.bankslips.rest.validator;

import org.springframework.stereotype.Component;

import com.bankslips.exception.BankslipBadRequestException;
import com.bankslips.exception.BankslipInvalidFieldsException;
import com.bankslips.rest.dto.BankSlipDTO;

/**
 * The Interface IBankslipValidator.
 */
@Component
public interface IBankslipValidator {
	
	/**
	 * Validate fields.
	 *
	 * @param request the request
	 * @throws BankslipInvalidFieldsException the bankslip invalid fields exception
	 */
	void validateFields(final BankSlipDTO request) throws BankslipInvalidFieldsException;
	
	/**
	 * Validate id.
	 *
	 * @param id the id
	 * @throws BankslipBadRequestException the bankslip bad request exception
	 */
	void validateId(final String id) throws BankslipBadRequestException;

}
