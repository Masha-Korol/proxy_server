package com.dsr.proxy_server.data.dto;

import com.dsr.proxy_server.data.enums.TimeUnit;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This class represents request, that somebody sends to change the servers check time interval
 */
public class ChangeServersCheckTimingRequest {

    @NotNull
    private TimeUnit timeUnit;
    @Min(1)
    private Integer interval;

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangeServersCheckTimingRequest that = (ChangeServersCheckTimingRequest) o;
        return timeUnit == that.timeUnit &&
                Objects.equals(interval, that.interval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeUnit, interval);
    }

    @Override
    public String toString() {
        return "ChangeCheckingTimingRequest{" +
                "timeUnit=" + timeUnit +
                ", interval=" + interval +
                '}';
    }
}
