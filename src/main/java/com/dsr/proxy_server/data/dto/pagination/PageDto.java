package com.dsr.proxy_server.data.dto.pagination;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public class PageDto<T> {

    private List<T> content;
    private long totalCount;

    public PageDto(Page<T> page) {
       this.content = page.getContent();
       this.totalCount = page.getTotalElements();
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDto<?> pageDto = (PageDto<?>) o;
        return Objects.equals(content, pageDto.content) &&
                Objects.equals(totalCount, pageDto.totalCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, totalCount);
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "content=" + content +
                ", totalCount=" + totalCount +
                '}';
    }
}
