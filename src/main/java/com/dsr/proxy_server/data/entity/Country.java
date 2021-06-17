package com.dsr.proxy_server.data.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "countries", schema = "public")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer countryId;
    private String nameEn;

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

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
        Country country = (Country) o;
        return Objects.equals(nameEn, country.nameEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameEn);
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryId=" + countryId +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }
}
