package com.bankslips.rest.validator;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.bankslips.rest.message.MessageInfo;
import com.bankslips.service.IServiceMessage;

/**
 * The Class ValidationAbstract.
 */
public abstract class ValidationAbstract {

	/**
	 * Validate required fields.
	 *
	 * @param object the object
	 * @param errorList the error list
	 * @param messagesUtil the messages util
	 */
	protected void validateRequiredFields(final Object object, final List<MessageInfo> errorList,
			final IServiceMessage messagesUtil) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
		for (final ConstraintViolation<Object> constraintViolation : constraintViolations) {
			final MessageInfo mensagem = messagesUtil.getServiceMessage(constraintViolation.getMessage());
			errorList.add(mensagem);
		}
	}
}
