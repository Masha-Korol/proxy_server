package com.dsr.proxy_server.data.dto.proxy_creation;

import java.util.Objects;

public class ProxyServerCreationResponseDto {

    private String message;

    public ProxyServerCreationResponseDto() {

    }

    public ProxyServerCreationResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyServerCreationResponseDto that = (ProxyServerCreationResponseDto) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "ProxyCreationResponseDto{" +
                "message='" + message + '\'' +
                '}';
    }
}
