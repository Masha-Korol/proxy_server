package com.dsr.proxy_server.data.dto.proxy;

import com.dsr.proxy_server.data.enums.ContentType;
import com.dsr.proxy_server.data.enums.Method;
import java.util.Objects;

/**
 * This class represents users' requests
 */
public class ProxyServerRequest {

    private String country;
    private Method method;
    private String url;
    private ContentType contentType;
    private Object payload;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyServerRequest that = (ProxyServerRequest) o;
        return Objects.equals(country, that.country) &&
                method == that.method &&
                Objects.equals(url, that.url) &&
                contentType == that.contentType &&
                Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, method, url, contentType, payload);
    }

    @Override
    public String toString() {
        return "ProxyServerRequest{" +
                "country=" + country +
                ", method=" + method +
                ", url='" + url + '\'' +
                ", contentType=" + contentType +
                ", payload=" + payload +
                '}';
    }
}
