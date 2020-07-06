package com.proxyprovider.repositories;

import com.proxyprovider.entities.Proxy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Abro on 6/20/2020.
 */
public interface ProxyRepository extends CrudRepository<Proxy, Integer>{

    @Query(value = "SELECT a FROM Proxy a WHERE (a.country.id = :country OR :country is NULL) AND (a.port = :port OR :port is NULL) " +
            " AND (a.anonymity.id = :anonymous OR :anonymous is NULL)" +
            " AND (a.protocol like %:proxyType% OR :proxyType is NULL OR :proxyType = '') ", nativeQuery = false)
    List<Proxy> findByParameters(@Param("country") Integer country,
                                 @Param("port") Integer port,
                                 @Param("anonymous") Integer anonymous,
                                 @Param("proxyType") String proxyType);


    @Query("SELECT DISTINCT a.port FROM Proxy a ORDER By a.port ASC")
    List<Integer> findAllByDistinct();
    Proxy findByIp(String ip);
}
