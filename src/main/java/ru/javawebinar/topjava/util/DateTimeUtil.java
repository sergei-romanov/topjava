package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T startIn, @Nullable T endNotIn) {
        return (startIn == null || value.compareTo(startIn) >= 0) && (endNotIn == null || value.compareTo(endNotIn) < 0);
    }

    public static LocalDate parseLocalDate(@Nullable String localDate) {
        return localDate != null ? LocalDate.parse(localDate) : null;
    }

    public static LocalTime parseLocalTime(@Nullable String localTime) {
        return localTime != null ? LocalTime.parse(localTime) : null;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

