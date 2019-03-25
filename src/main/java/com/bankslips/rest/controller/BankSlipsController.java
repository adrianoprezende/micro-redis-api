package com.bankslips.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bankslips.constants.MessageCodesConstants;
import com.bankslips.exception.BankslipBadRequestException;
import com.bankslips.exception.BankslipInvalidFieldsException;
import com.bankslips.exception.BankslipNotFoundException;
import com.bankslips.rest.dto.BankSlipDTO;
import com.bankslips.rest.message.StandardOutput;
import com.bankslips.rest.validator.IBankslipValidator;
import com.bankslips.service.IBankslipsService;
import com.bankslips.service.IServiceMessage;

@RestController
@RequestMapping("/rest/bankslips")
public class BankSlipsController {
	
	private static final Logger logger = LoggerFactory.getLogger(BankSlipsController.class);
	
	@Autowired
	private IServiceMessage messageUtils;
	
	@Autowired
	private IBankslipValidator validator;
	
	@Autowired
	private IBankslipsService service;
	
	@PostMapping
    public ResponseEntity<StandardOutput> create(@RequestBody BankSlipDTO request, UriComponentsBuilder ucBuilder) throws BankslipInvalidFieldsException {
        logger.info("Creating a new bank slip : {}", request);
        
        StandardOutput output = new StandardOutput();
        validator.validateFields(request);
    	BankSlipDTO response = service.save(request);
    	output.setObject(response);
    	output.setStatusCode(HttpStatus.CREATED.value());
    	output.setMessage(this.messageUtils.getMessage(MessageCodesConstants.BANKSLIP_CREATED));
        	
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }
	
	@GetMapping
	public ResponseEntity<StandardOutput> get(){
		logger.info("Get all bank slips");
		
		StandardOutput output = new StandardOutput();
		output.setObject(service.list());
		
    	output.setStatusCode(HttpStatus.OK.value());
    	output.setMessage(this.messageUtils.getMessage(MessageCodesConstants.OK));
        	
        return new ResponseEntity<>(output, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<StandardOutput> get(@PathVariable String id) throws BankslipBadRequestException, BankslipNotFoundException {
		logger.info("Get bank slip : {}", id);
		
		StandardOutput output = new StandardOutput();
		validator.validateId(id);
		output.setObject(service.find(id));
		
    	output.setStatusCode(HttpStatus.OK.value());
    	output.setMessage(this.messageUtils.getMessage(MessageCodesConstants.OK));
        	
        return new ResponseEntity<>(output, HttpStatus.OK);
	}
	
	@PutMapping("/{id}/pay")
	public ResponseEntity<StandardOutput> pay(@PathVariable String id) throws BankslipBadRequestException, BankslipNotFoundException {
		logger.info("Paying a bank slip with id: {}", id);
		
		StandardOutput output = new StandardOutput();
        validator.validateId(id);
		
		BankSlipDTO response = service.pay(id);
    	output.setObject(response);
    	output.setStatusCode(HttpStatus.OK.value());
    	output.setMessage(this.messageUtils.getMessage(MessageCodesConstants.BANKSLIP_PAID));
        	
        return new ResponseEntity<>(output, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/cancel")
	public ResponseEntity<StandardOutput> cancel(@PathVariable String id) throws BankslipBadRequestException, BankslipNotFoundException {
		logger.info("Canceling a bank slip with id: {}", id);
		
		StandardOutput output = new StandardOutput();
        validator.validateId(id);
		
		BankSlipDTO response = service.cancel(id);
    	output.setObject(response);
    	output.setStatusCode(HttpStatus.OK.value());
    	output.setMessage(this.messageUtils.getMessage(MessageCodesConstants.BANKSLIP_CANCELED));
        	
        return new ResponseEntity<>(output, HttpStatus.OK);
	}
}
