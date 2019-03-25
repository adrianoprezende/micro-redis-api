package com.bankslips.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.bankslips.constants.MessageCodesConstants;
import com.bankslips.enums.StatusEnum;
import com.bankslips.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipDTO implements Serializable {

	private static final long serialVersionUID = 4290363173090004717L;

	private String id;

	@NotBlank(message = MessageCodesConstants.DUE_DATE_IS_REQUIRED)
	@Pattern(regexp = DateUtils.REGEX_XML_DATE_FORMAT, message = MessageCodesConstants.INVALID_DUE_DATE)
	@JsonProperty("due_date")
	private String dueDate;

	@NotBlank(message = MessageCodesConstants.TOTAL_IS_REQUIRED)
	@Digits(integer = 19, fraction = 0, message = MessageCodesConstants.INVALID_TOTAL_IN_CENTS)
	@JsonProperty("total_in_cents")
	private String totalInCents;

	@NotBlank(message = MessageCodesConstants.CUSTOMER_IS_REQUIRED)
	private String customer;

	@NotBlank(message = MessageCodesConstants.STATUS_IS_REQUIRED)
	@Pattern(regexp = StatusEnum.REGEX_STATUS_ENUM, message = MessageCodesConstants.INVALID_STATUS)
	private String status;

	private String fine;
	
	public BankSlipDTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getTotalInCents() {
		return totalInCents;
	}

	public void setTotalInCents(String totalInCents) {
		this.totalInCents = totalInCents;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFine() {
		return fine;
	}

	public void setFine(String fine) {
		this.fine = fine;
	}

	@Override
	public String toString() {
		return "BankSlipDTO [id=" + id + ", dueDate=" + dueDate + ", totalInCents=" + totalInCents + ", customer="
				+ customer + ", status=" + status + ", fine=" + fine + "]";
	}

	public static class Builder {
		private String id;
		private String dueDate;
		private String totalInCents;
		private String customer;
		private String status;
		private String fine;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder dueDate(String dueDate) {
			this.dueDate = dueDate;
			return this;
		}

		public Builder totalInCents(String totalInCents) {
			this.totalInCents = totalInCents;
			return this;
		}

		public Builder customer(String customer) {
			this.customer = customer;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Builder fine(String fine) {
			this.fine = fine;
			return this;
		}

		public BankSlipDTO build() {
			return new BankSlipDTO(this);
		}
	}

	private BankSlipDTO(Builder builder) {
		this.id = builder.id;
		this.dueDate = builder.dueDate;
		this.totalInCents = builder.totalInCents;
		this.customer = builder.customer;
		this.status = builder.status;
		this.fine = builder.fine;
	}
}
