package com.medicinemanager.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private DateUtil() {
    }

    public static LocalDate parseDate(String text) throws DateTimeParseException {
        return LocalDate.parse(text.trim());
    }

    public static LocalTime parseTime(String text) throws DateTimeParseException {
        return LocalTime.parse(text.trim(), TIME_FORMAT);
    }
}
