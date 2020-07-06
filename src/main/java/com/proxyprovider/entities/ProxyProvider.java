package com.proxyprovider.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Abro on 6/28/2020.
 */
@Entity
public class ProxyProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String baseAddress;
    @OneToMany
    private Set<Proxy> proxies;
    private Date lastSuccessfull;
    private Date lastAttempt;

    @Column(columnDefinition = "integer default 0")
    private Integer numberOfRecords;
    private String errorIndication;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

    public Set<Proxy> getProxies() {
        return proxies;
    }

    public void setProxies(Set<Proxy> proxies) {
        this.proxies = proxies;
    }

    public Date getLastSuccessfull() {
        return lastSuccessfull;
    }

    public void setLastSuccessfull(Date lastSuccessfull) {
        this.lastSuccessfull = lastSuccessfull;
    }

    public Date getLastAttempt() {
        return lastAttempt;
    }

    public void setLastAttempt(Date lastAttempt) {
        this.lastAttempt = lastAttempt;
    }

    public Integer getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(Integer numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public String getErrorIndication() {
        return errorIndication;
    }

    public void setErrorIndication(String errorIndication) {
        this.errorIndication = errorIndication;
    }
}
