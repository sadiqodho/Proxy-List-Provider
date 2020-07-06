package com.proxyprovider.repositories;

import com.proxyprovider.entities.Country;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Abro on 6/25/2020.
 */

public interface CountryRepository extends CrudRepository<Country, Integer> {
    public Country findByAlpha2Code(String alpha2code);
}
