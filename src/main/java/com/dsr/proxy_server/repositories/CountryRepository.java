package com.dsr.proxy_server.repositories;

import com.dsr.proxy_server.data.entity.Country;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CountryRepository extends PagingAndSortingRepository<Country, Integer> {
    Country findByNameEn(String nameEn);
    boolean existsByNameEn(String nameEn);
}
