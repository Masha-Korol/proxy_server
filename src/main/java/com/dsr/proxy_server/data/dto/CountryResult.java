package com.dsr.proxy_server.data.dto;

import java.util.Objects;

public class CountryResult {

    private String nameEn;

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryResult that = (CountryResult) o;
        return Objects.equals(nameEn, that.nameEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameEn);
    }

    @Override
    public String toString() {
        return "CountryResult{" +
                "nameEn='" + nameEn + '\'' +
                '}';
    }
}

