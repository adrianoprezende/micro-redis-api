package com.bankslips.enums;

public enum StatusEnum {
	
	PENDING, PAID, CANCELED;
	
	public static final String REGEX_STATUS_ENUM = "^(PENDING|PAID|CANCELED)$";

}
