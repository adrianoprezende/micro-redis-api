package com.bankslips.service;

import java.util.List;

import com.bankslips.exception.BankslipNotFoundException;
import com.bankslips.rest.dto.BankSlipDTO;

public interface IBankslipsService {
	
	BankSlipDTO save(BankSlipDTO dto);
	
	List<BankSlipDTO> list();
	
	BankSlipDTO find(String id) throws BankslipNotFoundException;
	
	BankSlipDTO pay(String id) throws BankslipNotFoundException;

	BankSlipDTO cancel(String id) throws BankslipNotFoundException;

}
