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

    /*@Query(value = "from ProxyServer ps where ps.country =: country order by " +
            "case when ps.anonymity = 'None' then 0 " +
            "case when ps.anonymity = 'Unknown' rhen 1")
    @Query("SELECT e FROM Entity e ORDER BY (CASE WHEN e.enumField = 'ENUM_VALUE' THEN 0 else 1 END)")*/
    List<ProxyServer> findAllByCountry(@Param("country") Country country, Sort uptime);
}
