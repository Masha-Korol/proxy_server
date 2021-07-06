package com.dsr.proxy_server.data.dto.pagination;

import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class PageRequestDto {

    @NotNull(message = "value sort cannot be null")
    private Sort sort;
    @NotNull(message = "value page cannot be null")
    @Min(value = 1, message = "min value for page is 1")
    private Integer page;
    @NotNull(message = "value itemsPerPage cannot be null")
    @Min(value = 1, message = "min value for itemsPerPage is 1")
    private Integer itemsPerPage;

    public PageRequestDto(String sort, Integer page, Integer itemsPerPage) {
        if (sort != null) {
            switch (sort) {
                case "type":
                    this.sort = Sort.by("type");
                    break;
                case "anonymity":
                    this.sort = Sort.by("anonymity");
                    break;
                case "uptime":
                    this.sort = Sort.by("uptime");
                    break;
                case "port":
                    this.sort = Sort.by("port");
                    break;
                case "workload":
                    this.sort = Sort.by("workload");
                    break;
            }
        }

        this.page = page;
        this.itemsPerPage = itemsPerPage;
    }

    public Sort getSort() {
        return sort;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageRequestDto that = (PageRequestDto) o;
        return Objects.equals(sort, that.sort) &&
                Objects.equals(page, that.page) &&
                Objects.equals(itemsPerPage, that.itemsPerPage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sort, page, itemsPerPage);
    }

    @Override
    public String toString() {
        return "PageRequestDto{" +
                "sort=" + sort +
                ", page=" + page +
                ", itemsPerPage=" + itemsPerPage +
                '}';
    }
}
