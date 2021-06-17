package com.dsr.proxy_server.data.dto;

import com.dsr.proxy_server.data.enums.ProxyAnonymity;
import com.dsr.proxy_server.data.enums.YesNoAny;

import java.util.Objects;

public class ProxyResultItem {

    private String ip;
    private Integer port;
    private Integer type;
    private ProxyAnonymity anonymity;
    private Double uptime;
    private CountryResult country;
    private YesNoAny available;

    public YesNoAny getAvailable() {
        return available;
    }

    public void setAvailable(YesNoAny available) {
        this.available = available;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ProxyAnonymity getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(ProxyAnonymity anonymity) {
        this.anonymity = anonymity;
    }

    public Double getUptime() {
        return uptime;
    }

    public void setUptime(Double uptime) {
        this.uptime = uptime;
    }

    public CountryResult getCountry() {
        return country;
    }

    public void setCountry(CountryResult country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyResultItem that = (ProxyResultItem) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port) &&
                Objects.equals(type, that.type) &&
                anonymity == that.anonymity &&
                Objects.equals(uptime, that.uptime) &&
                Objects.equals(country, that.country) &&
                available == that.available;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, type, anonymity, uptime, country, available);
    }

    @Override
    public String toString() {
        return "ProxyResultItem{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", type=" + type +
                ", anonymity=" + anonymity +
                ", uptime=" + uptime +
                ", country=" + country +
                ", available=" + available +
                '}';
    }
}
