package com.abc;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;

public class DateProvider {
    private static DateProvider instance = new DateProvider();

    public static DateProvider getInstance() {
        return instance;
    }

    public static void setInstance(DateProvider dateProvider) {
        instance = dateProvider;
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
