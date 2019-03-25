package com.bankslips.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;

public final class DateUtils {

	public static final String REGEX_XML_DATE_FORMAT = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";

	/** Constant of XML date format : yyyy-MM-dd. */
	public static final String XML_DATE_FORMAT = "yyyy-MM-dd";

	private DateUtils() {
	}

	public static LocalDate getDate(final String stringDate, final String format) {
		if (StringUtils.isEmpty(stringDate)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDate.parse(stringDate, formatter);
	}
	
	public static String getDate(final LocalDate date, final String format) {
		if (date == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return date.format(formatter);
	}
	
	public static LocalDate getDateFromXMLFormat(final String stringDate) {
		return getDate(stringDate, XML_DATE_FORMAT);
	}
	
	public static String getDateToXMLFormat(final LocalDate date) {
		return getDate(date, XML_DATE_FORMAT);
	}

}
