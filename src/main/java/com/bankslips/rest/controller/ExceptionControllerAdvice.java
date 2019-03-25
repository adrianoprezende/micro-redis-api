package com.bankslips.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import com.bankslips.constants.MessageCodesConstants;
import com.bankslips.exception.AbstractException;
import com.bankslips.exception.BankslipBadRequestException;
import com.bankslips.exception.BankslipInvalidFieldsException;
import com.bankslips.exception.BankslipNotFoundException;
import com.bankslips.rest.message.StandardOutput;
import com.bankslips.service.IServiceMessage;

/**
 * The Class ExceptionControllerAdvice.
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
	
	@Autowired
	private IServiceMessage messageUtils;
	
	/**
	 * Exception handler.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(BankslipBadRequestException.class)
	public ResponseEntity<StandardOutput> exceptionHandler(BankslipBadRequestException ex) {
		return getFilledResponseEntity(ex);
	}
	
	/**
	 * Exception handler.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(BankslipInvalidFieldsException.class)
	public ResponseEntity<StandardOutput> exceptionHandler(BankslipInvalidFieldsException ex) {
		logger.error(ex.getMessage(), ex);
		StandardOutput output = getFilledStandardOutput(ex);
		output.setListError(ex.getListErros());
		return new ResponseEntity<>(output, ex.getStatus());
	}
	
	/**
	 * Exception handler.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(BankslipNotFoundException.class)
	public ResponseEntity<StandardOutput> exceptionHandler(BankslipNotFoundException ex) {
		return getFilledResponseEntity(ex);
	}
	
	/**
	 * Exception handler.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardOutput> exceptionHandler(HttpMessageNotReadableException ex) {
		logger.error(ex.getMessage(), ex);
		return getOutputForBadRequestNotFound();
	}
	
	/**
	 * Exception handler.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<StandardOutput> exceptionHandler(HttpMediaTypeNotSupportedException ex) {
		logger.error(ex.getMessage(), ex);
		return getOutputForBadRequestNotFound();
	}

	/**
	 * Gets the output for bad request not found.
	 *
	 * @return the output for bad request not found
	 */
	private ResponseEntity<StandardOutput> getOutputForBadRequestNotFound() {
		StandardOutput output = new StandardOutput();
		output.setStatusCode(HttpStatus.BAD_REQUEST.value());
		output.setMessage(messageUtils.getMessage(MessageCodesConstants.BANKSLIP_NOT_PROVIDED));
		return new ResponseEntity<>(output, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Exception handler.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardOutput> exceptionHandler(Exception ex) {
		logger.error(ex.getMessage(), ex);
		StandardOutput output = new StandardOutput();
		output.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		output.setMessage(ex.getMessage());
		return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Gets the filled response entity.
	 *
	 * @param ex the ex
	 * @return the filled response entity
	 */
	private ResponseEntity<StandardOutput> getFilledResponseEntity(AbstractException ex) {
		logger.error(ex.getMessage(), ex);
		return new ResponseEntity<>(getFilledStandardOutput(ex), ex.getStatus());
	}

	/**
	 * Gets the filled standard output.
	 *
	 * @param ex the ex
	 * @return the filled standard output
	 */
	private StandardOutput getFilledStandardOutput(AbstractException ex) {
		StandardOutput output = new StandardOutput();
		output.setStatusCode(ex.getStatus().value());
		output.setMessage(ex.getMessage());
		return output;
	}
}
