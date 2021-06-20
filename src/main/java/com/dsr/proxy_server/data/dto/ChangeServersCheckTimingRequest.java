package com.dsr.proxy_server.data.dto;

import com.dsr.proxy_server.data.enums.TimeUnit;

import java.util.Objects;

public class ChangeServersCheckTimingRequest {

    private TimeUnit timeUnit;
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