package br.dubidubibank.utils;

import br.dubidubibank.dtos.TimeRangeDto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class DateUtils {
    public static boolean isTimeRangesOverlap(TimeRangeDto oneTimeRange, TimeRangeDto otherTimeRange) {
        return (oneTimeRange.getStartTime().isBefore(otherTimeRange.getEndTime()) //
                || oneTimeRange.getStartTime().isEqual(otherTimeRange.getEndTime())) //
                && (oneTimeRange.getEndTime().isAfter(otherTimeRange.getStartTime()) //
                || oneTimeRange.getEndTime().isEqual(otherTimeRange.getStartTime()));
    }

    public static boolean isToday(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDate().equals(LocalDate.now(zonedDateTime.getZone()));
    }
}
