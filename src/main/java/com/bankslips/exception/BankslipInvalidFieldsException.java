package com.bankslips.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.bankslips.rest.message.MessageInfo;

public class BankslipInvalidFieldsException extends AbstractException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7289710326835508967L;
	
	/**
	 * Contains the error messages list from every busines layer.
	 */
	private final List<MessageInfo> listErros = new ArrayList<>();

	/**
	 * Instantiates a new business exception.
	 *
	 * @param message the message
	 */
	public BankslipInvalidFieldsException(final String message) {
		super(HttpStatus.UNPROCESSABLE_ENTITY, message);
	}

	/**
	 * Instantiates a new business exception.
	 *
	 * @param listErros the list erros
	 */
	public BankslipInvalidFieldsException(final List<MessageInfo> listErros, final String message) {
		super(HttpStatus.UNPROCESSABLE_ENTITY, message);
		addErrors(listErros);
	}

	/**
	 * Gets the list erros.
	 *
	 * @return the list erros
	 */
	public List<MessageInfo> getListErros() {
		return listErros;
	}

	/**
	 * Adds the errors.
	 *
	 * @param errors the errors
	 */
	public void addErrors(final List<MessageInfo> errors) {
		listErros.addAll(errors);
	}

	/**
	 * Adds the error.
	 *
	 * @param error the error
	 */
	public void addError(final MessageInfo error) {
		listErros.add(error);
	}

}
