package com.bankslips.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.bankslips.entity.BankSlip;

/**
 * The Class BankslipFineCalculator.
 */
public class BankslipFineCalculator {
	
	/** The Constant TEN_DAYS. */
	private static final long TEN_DAYS = 10l;
	
	/** The Constant HUNDRED_PERCENT_DIVIDER. */
	private static final int HUNDRED_PERCENT_DIVIDER = 100;
	
	/** The Constant HALF_PERCENT_DIVIDER. */
	private static final int HALF_PERCENT_DIVIDER = 2;
	
	/** The Constant ONE_PERCENT_MULTIPLIER. */
	private static final int ONE_PERCENT_MULTIPLIER = 1;
	
	/**
	 * Instantiates a new bankslip fine calculator.
	 */
	private BankslipFineCalculator() {
	}
	
	/**
	 * Verify apply fine.
	 *
	 * @param dueDate the due date
	 * @param totalInCents the total in cents
	 * @return the integer
	 */
	public static void verifyApplyFine(final BankSlip bankslip) {
		int fine = 0;
		
		if(isLate(bankslip.getDueDate())) {
			long daysBetween =  ChronoUnit.DAYS.between(bankslip.getDueDate(), LocalDate.now());
			
			if(daysBetween > TEN_DAYS) {
				fine = bankslip.getTotalInCents() * ONE_PERCENT_MULTIPLIER / HUNDRED_PERCENT_DIVIDER;
			} else {
				fine = bankslip.getTotalInCents() / HALF_PERCENT_DIVIDER / HUNDRED_PERCENT_DIVIDER;
			}
		}
		
		bankslip.setFine(fine);
	}
	
	/**
	 * Checks if is late.
	 *
	 * @param dueDate the due date
	 * @return true, if is late
	 */
	private static boolean isLate(final LocalDate dueDate) {
		return dueDate.isBefore(LocalDate.now());
	}
}
