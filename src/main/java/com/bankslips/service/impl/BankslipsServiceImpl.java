package com.bankslips.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bankslips.adapter.BankslipsAdapterIn;
import com.bankslips.adapter.BankslipsAdapterOut;
import com.bankslips.constants.MessageCodesConstants;
import com.bankslips.entity.BankSlip;
import com.bankslips.enums.StatusEnum;
import com.bankslips.exception.BankslipNotFoundException;
import com.bankslips.repository.BankslipsRepository;
import com.bankslips.rest.dto.BankSlipDTO;
import com.bankslips.service.IBankslipsService;
import com.bankslips.service.IServiceMessage;
import com.bankslips.utils.BankslipFineCalculator;

/**
 * The Class BankslipsServiceImpl.
 */
@Component
public class BankslipsServiceImpl implements IBankslipsService {
	
	/** The Constant logger. */
	public static final Logger logger = LoggerFactory.getLogger(BankslipsServiceImpl.class);
	
	@Autowired
	private IServiceMessage messageUtils;
	
	/** The repository. */
	@Autowired
	private BankslipsRepository repository;
	
	/** The adapter in. */
	private BankslipsAdapterIn adapterIn = new BankslipsAdapterIn();
	
	/** The adapter out. */
	private BankslipsAdapterOut adapterOut = new BankslipsAdapterOut();
	
	/* (non-Javadoc)
	 * @see com.bankslips.service.BankslipsService#save(com.bankslips.rest.dto.BankSlipDTO)
	 */
	@Override
	public BankSlipDTO save(final BankSlipDTO dtoIn) {
		logger.info("Input DTO to persist {}", dtoIn);
		
		BankSlip entity = adapterIn.toEntity(dtoIn);
		
		entity = repository.save(entity);
		
		BankSlipDTO dtoOut = adapterOut.toEntity(entity);
		
		logger.info("Output DTO persisted {}", dtoOut);
		
		return dtoOut;
	}

	@Override
	public List<BankSlipDTO> list() {
		logger.info("Listing all bankslips");
		
		List<BankSlipDTO> dtoList = adapterOut.toEntityList(repository.findAll());
		
		logger.info("Output DTO listed {}", dtoList);
		
		return dtoList;
	}

	@Override
	public BankSlipDTO find(String id) throws BankslipNotFoundException {
		logger.info("Finding bankslip with ID={}", id);
		
		BankSlip bankslip = findEntity(id);
		
		BankslipFineCalculator.verifyApplyFine(bankslip);
		
		BankSlipDTO dto = adapterOut.toEntity(bankslip);
		
		logger.info("Output DTO found {}", dto);
		
		return dto;
	}

	@Override
	public BankSlipDTO pay(String id) throws BankslipNotFoundException {
		logger.info("Paying bankslip with ID={}", id);
		
		BankSlip bankslip = findEntity(id);
		
		updateStatus(bankslip, StatusEnum.PAID);
		
		BankSlipDTO dto = adapterOut.toEntity(bankslip);
		
		logger.info("Output DTO payed {}", dto);
		
		return dto;
	}
	
	@Override
	public BankSlipDTO cancel(String id) throws BankslipNotFoundException {
		logger.info("Canceling bankslip with ID={}", id);
		
		BankSlip bankslip = findEntity(id);
		
		updateStatus(bankslip, StatusEnum.CANCELED);
		
		BankSlipDTO dto = adapterOut.toEntity(bankslip);
		
		logger.info("Output DTO canceled {}", dto);
		
		return dto;
	}

	private void updateStatus(final BankSlip bankslip, final StatusEnum newStatus) {
		bankslip.setStatus(newStatus.name());
		repository.save(bankslip);
	}

	private BankSlip findEntity(String id) throws BankslipNotFoundException {
		Optional<BankSlip> entity = repository.findById(id);
		
		if(!entity.isPresent()) {
			throw new BankslipNotFoundException(this.messageUtils.getMessage(MessageCodesConstants.BANKSLIP_NOT_FOUND));
		}
		return entity.get();
	}

}
