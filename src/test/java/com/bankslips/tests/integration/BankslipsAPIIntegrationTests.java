package com.bankslips.tests.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.bankslips.enums.StatusEnum;
import com.bankslips.main.SpringBootRestApiApp;
import com.bankslips.rest.dto.BankSlipDTO;
import com.bankslips.rest.message.StandardOutput;
import com.bankslips.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringBootRestApiApp.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class BankslipsAPIIntegrationTests {

	private Random random = new Random();

	private static final String API_BASE_URL = "/rest/bankslips";
	
	ObjectMapper objectMaper = new ObjectMapper();
	
	@Autowired
	private TestRestTemplate restTemplate;

	private BankSlipDTO createPendindBankslip() {
		return new BankSlipDTO.Builder().customer("Customer" + random.nextInt()).dueDate(getDateFormatedPlusDays(3))
				.status(StatusEnum.PENDING.name()).totalInCents(String.valueOf(random.nextInt())).build();
	}

	private String getDateFormatedPlusDays(int days) {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.XML_DATE_FORMAT);
		localDate = localDate.plusDays(days);
		String dateFormated = localDate.format(formatter);
		return dateFormated;
	}

	@Test
	public void shouldReturnCreatedWhenDoAPostRequestWithPendingBankslip() {
		ResponseEntity<StandardOutput> responseEntity = restTemplate.postForEntity(API_BASE_URL, createPendindBankslip(),
				StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(output.getObject());
		assertEquals("Bankslip created", output.getMessage());
		assertNull(output.getListError());
		
		BankSlipDTO responseObject = objectMaper.convertValue(output.getObject(), BankSlipDTO.class);
		assertEquals(StatusEnum.PENDING.name(), responseObject.getStatus());
	}
	
	@Test
	public void shouldReturnBankslipNotProvidedWhenDoAPostRequest() {
		ResponseEntity<StandardOutput> responseEntity = restTemplate.postForEntity(API_BASE_URL, null,
				StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(output.getObject());
		assertEquals("Bankslip not provided in the request body", output.getMessage());
	}
	
	@Test
	public void shouldReturnInvalidBankslipProvidedWhenDoAPostRequest() {
		BankSlipDTO request = createPendindBankslip();
		request.setDueDate(null);
		request.setStatus(null);
		request.setCustomer(null);
		request.setTotalInCents(null);
		
		ResponseEntity<StandardOutput> responseEntity = restTemplate.postForEntity(API_BASE_URL, request,
				StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertNull(output.getObject());
		assertEquals("Invalid bankslip provided.", output.getMessage());
		assertNotNull(output.getListError());
		assertEquals(4, output.getListError().size());
	}
	
	@Test
	public void shouldReturnOKWhenDoAGetRequest() {
		ResponseEntity<StandardOutput> responseEntity = restTemplate.getForEntity(API_BASE_URL, StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(output.getObject());
		assertEquals("Ok", output.getMessage());
		assertNull(output.getListError());
	}
	
	@Test
	public void shouldReturnInvalidIDProvidedWhenDoAGetRequest() {
		ResponseEntity<StandardOutput> responseEntity = restTemplate.getForEntity(API_BASE_URL+"/88178865-15a8-4dc9-9f21-f202", StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(output.getObject());
		assertEquals("Invalid id provided - it must be a valid UUID", output.getMessage());
	}
	
	@Test
	public void shouldReturnBankslipNotFoundWhenDoAGetRequest() {
		ResponseEntity<StandardOutput> responseEntity = restTemplate.getForEntity(API_BASE_URL+"/379a345e-2439-4f7e-a486-73bb5d011930", StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(output.getObject());
		assertEquals("Bankslip not found with the specified id", output.getMessage());
	}
	
	@Test
	public void shouldReturnBankslipPayedWhenDoAPutRequest() {
		ResponseEntity<StandardOutput> postResponseEntity = restTemplate.postForEntity(API_BASE_URL, createPendindBankslip(),
				StandardOutput.class);
		StandardOutput postOutput = postResponseEntity.getBody();
		
		BankSlipDTO postResponseObject = objectMaper.convertValue(postOutput.getObject(), BankSlipDTO.class);
		
		ResponseEntity<StandardOutput> responseEntity = restTemplate.exchange(API_BASE_URL+"/"+ postResponseObject.getId() +"/pay", 
				HttpMethod.PUT, null, StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(output.getObject());
		assertEquals("Bankslip paid", output.getMessage());
		
		BankSlipDTO responseObject = objectMaper.convertValue(output.getObject(), BankSlipDTO.class);
		assertEquals(StatusEnum.PAID.name(), responseObject.getStatus());
	}
	
	@Test
	public void shouldReturnBankslipNotFoundWhenDoAPutRequest() {
		ResponseEntity<StandardOutput> responseEntity = restTemplate.exchange(API_BASE_URL+"/379a345e-2439-4f7e-a486-73bb5d011930/pay", 
				HttpMethod.PUT, null, StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(output.getObject());
		assertEquals("Bankslip not found with the specified id", output.getMessage());
	}
	
	
	@Test
	public void shouldReturnBankslipCanceledWhenDoADeleteRequest() {
		ResponseEntity<StandardOutput> postResponseEntity = restTemplate.postForEntity(API_BASE_URL, createPendindBankslip(),
				StandardOutput.class);
		StandardOutput postOutput = postResponseEntity.getBody();
		
		BankSlipDTO postResponseObject = objectMaper.convertValue(postOutput.getObject(), BankSlipDTO.class);
		
		ResponseEntity<StandardOutput> responseEntity = restTemplate.exchange(API_BASE_URL+"/"+ postResponseObject.getId() +"/cancel", 
				HttpMethod.DELETE, null, StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(output.getObject());
		assertEquals("Bankslip canceled", output.getMessage());
		
		BankSlipDTO responseObject = objectMaper.convertValue(output.getObject(), BankSlipDTO.class);
		assertEquals(StatusEnum.CANCELED.name(), responseObject.getStatus());
	}
	
	@Test
	public void shouldReturnBankslipNotFoundWhenDoADeleteRequest() {
		ResponseEntity<StandardOutput> responseEntity = restTemplate.exchange(API_BASE_URL+"/379a345e-2439-4f7e-a486-73bb5d011930/cancel", 
				HttpMethod.DELETE, null, StandardOutput.class);
		StandardOutput output = responseEntity.getBody();
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(output.getObject());
		assertEquals("Bankslip not found with the specified id", output.getMessage());
	}
	

}
