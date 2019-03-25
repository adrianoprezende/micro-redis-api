package com.bankslips.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TB_BANKSLIPS")
public class BankSlip {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "id")
	private String id;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "total_in_cents")
	private Integer totalInCents;

	@Column(name = "customer")
	private String customer;

	@Column(name = "status")
	private String status;

	@Transient
	private int fine;
	
	public BankSlip() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getTotalInCents() {
		return totalInCents;
	}

	public void setTotalInCents(Integer totalInCents) {
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

	public int getFine() {
		return fine;
	}

	public void setFine(Integer fine) {
		this.fine = fine;
	}

	public static class Builder {
		private String id;
		private LocalDate dueDate;
		private Integer totalInCents;
		private String customer;
		private String status;
		private int fine;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder dueDate(LocalDate dueDate) {
			this.dueDate = dueDate;
			return this;
		}

		public Builder totalInCents(Integer totalInCents) {
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

		public Builder fine(int fine) {
			this.fine = fine;
			return this;
		}

		public BankSlip build() {
			return new BankSlip(this);
		}
	}

	private BankSlip(Builder builder) {
		this.id = builder.id;
		this.dueDate = builder.dueDate;
		this.totalInCents = builder.totalInCents;
		this.customer = builder.customer;
		this.status = builder.status;
		this.fine = builder.fine;
	}
}
