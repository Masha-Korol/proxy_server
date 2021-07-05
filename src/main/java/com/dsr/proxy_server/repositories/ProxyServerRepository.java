package com.dsr.proxy_server.repositories;

import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.YesNoAny;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.*;

public interface ProxyServerRepository extends PagingAndSortingRepository<ProxyServer, Integer> {
    boolean existsByIp(String ip);

    List<ProxyServer> findAllByCountry(Country country);

    ProxyServer findByIp(String ip);

    Page<ProxyServer> findAll(Pageable pageable);
}
