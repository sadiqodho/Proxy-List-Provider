package com.proxyprovider.repositories;

import com.proxyprovider.entities.Anonymity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Abro on 6/25/2020.
 */

public interface AnonymityRepository extends CrudRepository<Anonymity, Integer> {
    @Query("SELECT a FROM Anonymity a WHERE a.id = :id")
    Anonymity findWithId(@Param("id") Integer id);
}