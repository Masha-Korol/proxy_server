package com.dsr.proxy_server.data.dto.proxy_creation;

import com.dsr.proxy_server.data.enums.ProxyAnonymity;
import com.dsr.proxy_server.data.enums.ProxyProtocol;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ProxyServerCreationDto {

    @NotBlank
    private String ip;
    @NotNull
    private Integer port;
    @NotNull
    private ProxyProtocol type;
    @NotBlank
    private String countryName;
    @NotNull
    private ProxyAnonymity anonymity;
    @NotNull
    private Double uptime;

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

    public ProxyProtocol getType() {
        return type;
    }

    public void setType(ProxyProtocol type) {
        this.type = type;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyServerCreationDto that = (ProxyServerCreationDto) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port) &&
                type == that.type &&
                Objects.equals(countryName, that.countryName) &&
                anonymity == that.anonymity &&
                Objects.equals(uptime, that.uptime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, type, countryName, anonymity, uptime);
    }

    @Override
    public String toString() {
        return "ProxyServerCreationDto{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", type=" + type +
                ", countryName='" + countryName + '\'' +
                ", anonymity=" + anonymity +
                ", uptime=" + uptime +
                '}';
    }
}
