package com.dsr.proxy_server.repositories;

import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.data.entity.ProxyServer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface ProxyServerRepository extends PagingAndSortingRepository<ProxyServer, Integer> {
    boolean existsByIp(String ip);
    List<ProxyServer> findAllByCountry(Country country);
}
