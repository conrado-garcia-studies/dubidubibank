package br.dubidubibank.entities;

import br.dubidubibank.dtos.TimeRangeDto;
import br.dubidubibank.utils.DateUtils;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class Limit extends Entity {
    private Command command;
    private String endTimestamp;
    private String startTimestamp;
    private Double value;

    public Limit() {
    }

    public Limit(Command command, String endTimestamp, String startTimestamp, Double value) {
        validateCommand(command);
        validateEndTimestamp(endTimestamp);
        validateStartTimestamp(startTimestamp);
        validateValue(value);
        this.command = command;
        this.endTimestamp = endTimestamp;
        this.startTimestamp = startTimestamp;
        this.value = value;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        validateCommand(command);
        this.command = command;
    }

    private void validateCommand(Command command) {
        if (!command.getLimitable()) {
            throw new IllegalArgumentException("The command is not valid.");
        }
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
        validateEndTimestamp(endTimestamp);
        this.endTimestamp = endTimestamp;
    }

    private void validateEndTimestamp(String endTimestamp) {
        if (isTimestampInvalid(endTimestamp)) {
            throw new IllegalArgumentException("The format of the end timestamp is not valid.");
        }
        if (startTimestamp != null && startTimestamp.compareTo(endTimestamp) >= 0) {
            throw new IllegalArgumentException("The end time must be at least 1 minute greater than the start time.");
        }
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        validateStartTimestamp(startTimestamp);
        this.startTimestamp = startTimestamp;
    }

    private void validateStartTimestamp(String startTimestamp) {
        if (isTimestampInvalid(startTimestamp)) {
            throw new IllegalArgumentException("The format of the start timestamp is not valid.");
        }
        if (endTimestamp != null && this.startTimestamp.compareTo(endTimestamp) >= 0) {
            throw new IllegalArgumentException("The end time must be at least 1 minute greater than the start time.");
        }
    }

    private boolean isTimestampInvalid(String timestamp) {
        if (timestamp != null && timestamp.length() == 5 && timestamp.charAt(2) == ':') {
            String hoursTimestamp = timestamp.substring(0, 2);
            String minutesTimestamp = timestamp.substring(3);
            try {
                int hours = Integer.parseInt(hoursTimestamp);
                int minutes = Integer.parseInt(minutesTimestamp);
                if (hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return true;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(Double value) {
        if (value == null || value < 0d) {
            throw new IllegalArgumentException("The value is not valid.");
        }
    }

    public Optional<Limit> getOverlappingLimit(Collection<Limit> others) {
        ZonedDateTime now = ZonedDateTime.now();
        TimeRangeDto oneTimeRange = getTimeRange(now);
        return others //
                .stream() //
                .filter(other -> {
                    if (other.getCommand().equals(command)) {
                        TimeRangeDto otherTimeRange = other.getTimeRange(now);
                        return DateUtils.isTimeRangesOverlap(oneTimeRange, otherTimeRange);
                    }
                    return false;
                }) //
                .findFirst();
    }

    public boolean isActive() {
        ZonedDateTime now = ZonedDateTime.now();
        TimeRangeDto timeRange = getTimeRange(now);
        return (now.isEqual(timeRange.getEndTime()) || now.isBefore(timeRange.getEndTime()) //
                && (now.isEqual(timeRange.getStartTime()) || now.isAfter(timeRange.getStartTime())));
    }

    private TimeRangeDto getTimeRange(ZonedDateTime now) {
        String endHoursTimestamp = endTimestamp.substring(0, 2);
        int endHours = Integer.parseInt(endHoursTimestamp);
        String endMinutesTimestamp = endTimestamp.substring(3);
        int endMinutes = Integer.parseInt(endMinutesTimestamp);
        ZonedDateTime endTime = now.withHour(endHours).withMinute(endMinutes).withSecond(0) //
                .withNano(0).plusMinutes(1).minusNanos(1L);
        String startHoursTimestamp = startTimestamp.substring(0, 2);
        int startHours = Integer.parseInt(startHoursTimestamp);
        String startMinutesTimestamp = startTimestamp.substring(3);
        int startMinutes = Integer.parseInt(startMinutesTimestamp);
        ZonedDateTime startTime = now.withHour(startHours).withMinute(startMinutes).withSecond(0) //
                .withNano(0);
        return new TimeRangeDto(endTime, startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Limit limit = (Limit) o;
        return Objects.equals(command, limit.command) && Objects.equals(endTimestamp, limit.endTimestamp) //
                && Objects.equals(startTimestamp, limit.startTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, endTimestamp, startTimestamp);
    }

    @Override
    public String toString() {
        return String.format("%s of $%.2f from %s to %s", command, value, startTimestamp, endTimestamp);
    }
}
