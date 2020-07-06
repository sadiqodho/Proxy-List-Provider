package com.proxyprovider.repositories;

import com.proxyprovider.entities.ProxyProvider;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Abro on 6/28/2020.
 */
public interface ProxyProviderRepository extends CrudRepository<ProxyProvider, Integer>{
    @Query("SELECT a FROM ProxyProvider a WHERE a.id = :id")
    ProxyProvider findByObjById(@Param("id") Integer id);
}
