package com.proxyprovider.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Abro on 6/20/2020.
 */
@Entity
public class Proxy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String ip;
    private Integer port;
    private String protocol;
    private Double speed;
    private Date lastFound;
    private Date firstFound;
    private Date lastBasicFunctionalityTest;
    private String testURL;

    @ManyToOne
    private Country country;

    @ManyToOne
    private ProxyProvider provider;

    @ManyToOne
    private Anonymity anonymity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Date getLastFound() {
        return lastFound;
    }

    public void setLastFound(Date lastFound) {
        this.lastFound = lastFound;
    }

    public Date getFirstFound() {
        return firstFound;
    }

    public void setFirstFound(Date firstFound) {
        this.firstFound = firstFound;
    }

    public Date getLastBasicFunctionalityTest() {
        return lastBasicFunctionalityTest;
    }

    public void setLastBasicFunctionalityTest(Date lastBasicFunctionalityTest) {
        this.lastBasicFunctionalityTest = lastBasicFunctionalityTest;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public ProxyProvider getProvider() {
        return provider;
    }

    public void setProvider(ProxyProvider provider) {
        this.provider = provider;
    }

    public Anonymity getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(Anonymity anonymity) {
        this.anonymity = anonymity;
    }

    public String getTestURL() {
        return testURL;
    }

    public void setTestURL(String testURL) {
        this.testURL = testURL;
    }
}
