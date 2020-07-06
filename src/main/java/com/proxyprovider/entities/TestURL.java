package com.proxyprovider.entities;

import javax.persistence.*;

/**
 * Created by Abro on 6/25/2020.
 */
@Entity
public class TestURL {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private ProxyProvider provider;
    private String validationDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProxyProvider getProvider() {
        return provider;
    }

    public void setProvider(ProxyProvider provider) {
        this.provider = provider;
    }

    public String getValidationDetails() {
        return validationDetails;
    }

    public void setValidationDetails(String validationDetails) {
        this.validationDetails = validationDetails;
    }
}
