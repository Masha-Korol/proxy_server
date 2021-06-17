package com.dsr.proxy_server.model;

import java.io.Serializable;
import java.util.*;

public class Response implements Serializable {

    private Integer pageNumber;
    private Integer pageCount;
    private List<ProxyResultItem> items;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<ProxyResultItem> getItems() {
        return items;
    }

    public void setItems(List<ProxyResultItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(pageNumber, response.pageNumber) &&
                Objects.equals(pageCount, response.pageCount) &&
                Objects.equals(items, response.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNumber, pageCount, items);
    }

    @Override
    public String toString() {
        return "Response{" +
                "pageNumber=" + pageNumber +
                ", pageCount=" + pageCount +
                ", items=" + items +
                '}';
    }
}
