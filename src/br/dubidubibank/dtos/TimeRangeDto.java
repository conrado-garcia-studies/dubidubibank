package br.dubidubibank.dtos;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class TimeRangeDto implements Serializable {
    private ZonedDateTime endTime;
    private ZonedDateTime startTime;

    public TimeRangeDto(ZonedDateTime endTime, ZonedDateTime startTime) {
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }
}
