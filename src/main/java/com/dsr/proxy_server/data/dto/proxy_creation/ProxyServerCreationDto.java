package com.dsr.proxy_server.data.dto.proxy_creation;

import com.dsr.proxy_server.data.enums.ProxyProtocol;

import java.util.Objects;

public class ProxyServerCreationDto {

    private String ip;
    private Integer port;
    private ProxyProtocol proxyProtocol;
    private String countryName;

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

    public ProxyProtocol getProxyProtocol() {
        return proxyProtocol;
    }

    public void setProxyProtocol(ProxyProtocol proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
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
                proxyProtocol == that.proxyProtocol &&
                Objects.equals(countryName, that.countryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, proxyProtocol, countryName);
    }

    @Override
    public String toString() {
        return "ProxyServerCreationDto{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", proxyProtocol=" + proxyProtocol +
                ", countryName='" + countryName + '\'' +
                '}';
    }
}
