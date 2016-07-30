package com.abc;

import org.joda.time.LocalDateTime;

import java.util.regex.Pattern;

public class TestUtils {
    public static final double PRECISION = 1e-15;

    public static void configureDateForDateProvider(final LocalDateTime toSet) {
        DateProvider.setInstance(new DateProvider(){
            @Override
            public LocalDateTime now() {
                return toSet;
            }
        });
    }

    public static Pattern getPatternForNumeric(Object value) {
        return Pattern.compile(String.format(".*%.2f.*", value), Pattern.DOTALL);
    }
}
