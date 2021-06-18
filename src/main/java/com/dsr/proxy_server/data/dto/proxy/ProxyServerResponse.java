package com.dsr.proxy_server.data.dto.proxy;

import java.util.Objects;

public class ProxyServerResponse {

    private String ip;
    private Integer statusCode;
    private String responseBody;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyServerResponse that = (ProxyServerResponse) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(statusCode, that.statusCode) &&
                Objects.equals(responseBody, that.responseBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, statusCode, responseBody);
    }

    @Override
    public String toString() {
        return "ProxyServerResponse{" +
                "ip='" + ip + '\'' +
                ", statusCode=" + statusCode +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
