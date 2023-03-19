package com.project.common.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static final String PLAIN_DATE_PATTERN = "yyyyMMdd";

    public static String convertToHumanRecognizable(LocalDateTime time){
        LocalDateTime now = LocalDateTime.now();
        String result = "";
        if(time.isAfter(now.minusHours(1L))){
            LocalDateTime nowByMinute = now.truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime createdByMinute = time.truncatedTo(ChronoUnit.MINUTES);
            if(createdByMinute.isAfter(nowByMinute.minusMinutes(1L))){
                result = "방금 전";
            }else{
                Duration duration = Duration.between(time, now);
                result = (duration.getSeconds() / 60) + "분 전";
            }
        }else if(time.truncatedTo(ChronoUnit.DAYS).isEqual(now.truncatedTo(ChronoUnit.DAYS))){
            result = time.format(DateTimeFormatter.ofPattern("hh시 mm분"));
        }else{
            result = time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        }
        return result;
    }

    public static LocalDateTime max(LocalDateTime one, LocalDateTime two) {
        if (one == null) {
            return two;
        }

        if (two == null) {
            return one;
        }

        if (one.isBefore(two)) {
            return two;
        } else {
            return one;
        }
    }

    public static LocalDateTime min(LocalDateTime one, LocalDateTime two) {
        if (one == null) {
            return two;
        }

        if (two == null) {
            return one;
        }

        if (one.isAfter(two)) {
            return two;
        } else {
            return one;
        }
    }

    public static LocalDateTime convertSimpleLongTypeDayToDateTime(long longTypeDay) {
        int year = (int) (longTypeDay / 10000);
        int temp = (int) (longTypeDay % 10000);
        int month = temp / 100;
        int day = temp % 100;

        return LocalDateTime.of(year, month, day, 0, 0);
    }

    public static Period getDateDiffByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        return Period.between(startDate.toLocalDate(), endDate.toLocalDate());
    }

    // 만 나이
    public static Integer getAmericanAge(String birthdayString) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime parsedBirthDate = LocalDateTime.parse(birthdayString, DateTimeFormatter.ofPattern(PLAIN_DATE_PATTERN));

        int americanAge = now.minusYears(parsedBirthDate.getYear()).getYear();

        if (parsedBirthDate.plusYears(americanAge).isAfter(now)) {
            americanAge = americanAge -1;
        }

        return americanAge;
    }

}
