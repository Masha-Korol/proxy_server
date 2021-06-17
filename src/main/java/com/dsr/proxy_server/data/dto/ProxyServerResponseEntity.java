package com.dsr.proxy_server.data.dto;

import java.util.Objects;

public class ProxyServerResponseEntity {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyServerResponseEntity that = (ProxyServerResponseEntity) o;
        return Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(response);
    }

    @Override
    public String toString() {
        return "ProxyServerResponseEntity{" +
                "response=" + response +
                '}';
    }
}
