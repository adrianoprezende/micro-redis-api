package com.bankslips.tests.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bankslips.enums.StatusEnum;
import com.bankslips.exception.BankslipNotFoundException;
import com.bankslips.main.SpringBootRestApiApp;
import com.bankslips.rest.dto.BankSlipDTO;
import com.bankslips.service.IBankslipsService;
import com.bankslips.utils.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootRestApiApp.class)
public class BankslipsServiceTests {
	
	@Autowired
	private IBankslipsService service;
	
	private Random random = new Random();
	
	private BankSlipDTO createPendindBankslip() {
		return new BankSlipDTO.Builder().customer("Customer" + random.nextInt()).dueDate(getDateFormatedPlusDays(3))
				.status(StatusEnum.PENDING.name()).totalInCents(String.valueOf(random.nextInt(Integer.MAX_VALUE)+1)).build();
	}
	
	private BankSlipDTO createExpiredBankslip_11days() {
		String dateFormated = getDateFormatedMinusDays(11);

		BankSlipDTO dto = createPendindBankslip();
		dto.setDueDate(dateFormated);

		return dto;
	}

	private BankSlipDTO createExpiredBankslip_4days() {
		String dateFormated = getDateFormatedMinusDays(4);

		BankSlipDTO dto = createPendindBankslip();
		dto.setDueDate(dateFormated);

		return dto;
	}
	
	private String getDateFormatedMinusDays(int days) {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.XML_DATE_FORMAT);
		localDate = localDate.minusDays(days);
		String dateFormated = localDate.format(formatter);
		return dateFormated;
	}
	
	private String getDateFormatedPlusDays(int days) {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.XML_DATE_FORMAT);
		localDate = localDate.plusDays(days);
		String dateFormated = localDate.format(formatter);
		return dateFormated;
	}
	
	@Test
	public void shouldSave() {
		BankSlipDTO response = service.save(createPendindBankslip());
		assertNotNull(response);
		assertNotNull(response.getId());
	}
	
	@Test
	public void shouldSaveAndApplyFineOfHalfPercent() throws BankslipNotFoundException {
		BankSlipDTO request = createExpiredBankslip_4days();
		BankSlipDTO saveResponse = service.save(request);
		assertNotNull(saveResponse);
		assertNotNull(saveResponse.getId());
		assertEquals(request.getTotalInCents(), saveResponse.getTotalInCents());
		
		BankSlipDTO response = service.find(saveResponse.getId());
		assertNotNull(response.getFine());
		
		Integer fineResult = calculateHalfPercentFine(request);
		
		assertEquals(String.valueOf(fineResult), response.getFine());
	}
	
	@Test
	public void shouldSaveAndApplyFineOfOnePercent() throws BankslipNotFoundException {
		BankSlipDTO request = createExpiredBankslip_11days();
		BankSlipDTO saveResponse = service.save(request);
		assertNotNull(saveResponse);
		assertNotNull(saveResponse.getId());
		assertEquals(request.getTotalInCents(), saveResponse.getTotalInCents());
		
		BankSlipDTO response = service.find(saveResponse.getId());
		assertNotNull(response.getFine());
		
		Integer fineResult = calculateOnePercentFine(request);
		
		assertEquals(String.valueOf(fineResult), response.getFine());
	}

	private Integer calculateOnePercentFine(BankSlipDTO request) {
		Integer fineResult = Integer.valueOf(request.getTotalInCents()) * 1 / 100;
		return fineResult;
	}
	
	@Test
	public void shouldNotApplyFineOfHalfPercent() throws BankslipNotFoundException {
		BankSlipDTO request = createExpiredBankslip_11days();
		BankSlipDTO saveResponse = service.save(request);
		assertNotNull(saveResponse);
		assertNotNull(saveResponse.getId());
		assertEquals(request.getTotalInCents(), saveResponse.getTotalInCents());
		
		BankSlipDTO response = service.find(saveResponse.getId());
		assertNotNull(response.getFine());
		
		Integer fineResult = calculateHalfPercentFine(request);
		
		assertNotEquals(String.valueOf(fineResult), response.getFine());
	}
	
	@Test
	public void shouldNotApplyFineOfOnePercent() throws BankslipNotFoundException {
		BankSlipDTO request = createExpiredBankslip_4days();
		BankSlipDTO saveResponse = service.save(request);
		assertNotNull(saveResponse);
		assertNotNull(saveResponse.getId());
		assertEquals(request.getTotalInCents(), saveResponse.getTotalInCents());
		
		BankSlipDTO response = service.find(saveResponse.getId());
		assertNotNull(response.getFine());
		
		Integer fineResult = calculateOnePercentFine(request);
		
		assertNotEquals(String.valueOf(fineResult), response.getFine());
	}

	private Integer calculateHalfPercentFine(BankSlipDTO request) {
		Integer fineResult = Integer.valueOf(request.getTotalInCents()) / 2 / 100;
		return fineResult;
	}

}
