package com.polus.fibicomp.util;

import org.joda.time.DateTime;

public final class InactivatableFromToUtils {

    private InactivatableFromToUtils() {
        throw new UnsupportedOperationException("do not call");
    }

    public static boolean isActive(DateTime activeFromDate, DateTime activeToDate, DateTime activeAsOfDate) {
        long asOfDate = System.currentTimeMillis();
        if (activeAsOfDate != null) {
            asOfDate = activeAsOfDate.getMillis();
        }

        return computeActive(activeFromDate, activeToDate, asOfDate);
    }

    private static boolean computeActive(DateTime activeFromDate, DateTime activeToDate, long asOfDate) {
        return (activeFromDate == null || asOfDate >= activeFromDate.getMillis()) &&
                (activeToDate == null || asOfDate < activeToDate.getMillis());
    }
}
