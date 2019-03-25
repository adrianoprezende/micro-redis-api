package com.bankslips.utils;

/**
 * The Class ObjectUtils.
 */
public class ObjectUtils {
	
	/**
	 * Instantiates a new object utils.
	 */
	private ObjectUtils() {
	}
	
	/**
	 * Safety value of.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String safetyValueOf(final Integer value) {
		if(value == null) {
			return null;
		}
		
		return String.valueOf(value);
	}
	
	/**
	 * Safety value of.
	 *
	 * @param value the value
	 * @return the integer
	 */
	public static Integer safetyValueOf(final String value) {
		if(value == null) {
			return null;
		}
		
		return Integer.valueOf(value);
	}
}
