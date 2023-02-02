package com.vincent.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@SuppressWarnings({"squid:S2885", "squid:S112"})
public class CommonUtil {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private CommonUtil() {
    }

    public static Date toDate(String dateStr) {
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toSearchText(String s) {
        return Optional.ofNullable(s)
                .map(String::trim)
                .map(String::toLowerCase)
                .orElse("");
    }
}
