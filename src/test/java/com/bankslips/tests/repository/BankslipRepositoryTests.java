package com.bankslips.tests.repository;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bankslips.entity.BankSlip;
import com.bankslips.enums.StatusEnum;
import com.bankslips.main.SpringBootRestApiApp;
import com.bankslips.repository.BankslipsRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = SpringBootRestApiApp.class)
public class BankslipRepositoryTests {
	
	@Autowired
	private BankslipsRepository repository;
	
	private Random random = new Random();
	
	private BankSlip createPendindBankslip() {
		return new BankSlip.Builder().customer("Customer" + random.nextInt()).dueDate(getDateFormatedPlusDays(3))
				.status(StatusEnum.PENDING.name()).totalInCents(random.nextInt()).build();
	}
	
	private LocalDate getDateFormatedPlusDays(int days) {
		LocalDate localDate = LocalDate.now();
		return localDate.plusDays(days);
	}
	
	@Test
	public void shouldSaveAndFindAll() {
		repository.save(createPendindBankslip());
		repository.save(createPendindBankslip());
		// when
		List<BankSlip> bankslips = repository.findAll();
		// then
		assertEquals(2, bankslips.size());
	}
	

}
