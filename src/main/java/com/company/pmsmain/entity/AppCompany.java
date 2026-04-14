package com.company.pmsmain.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@JmixEntity
@Table(name = "APP_COMPANY")
@Entity
public class AppCompany {

    @JmixGeneratedValue
    @Id
    @Column(name = "ID", nullable = false)
    private UUID id;

    @InstanceName
    @Column(name = "COMPANY_CODE", nullable = false, unique = true, length = 50)
    private String companyCode;

    @Column(name = "COMPANY_NAME", nullable = false, length = 200)
    private String companyName;

    @Column(name = "SUBDOMAIN", unique = true, length = 100)
    private String subdomain;

    @Column(name = "PATH_KEY", unique = true, length = 100)
    private String pathKey;

    @Column(name = "DB_TYPE", nullable = false, length = 30)
    private String dbType;

    @Column(name = "DB_HOST", nullable = false, length = 200)
    private String dbHost;

    @Column(name = "DB_PORT", nullable = false)
    private Integer dbPort;

    @Column(name = "DB_NAME", nullable = false, length = 200)
    private String dbName;

    @Column(name = "DB_USERNAME", nullable = false, length = 200)
    private String dbUsername;

    @Column(name = "DB_PASSWORD_ENC", nullable = false, length = 1000)
    private String dbPasswordEnc;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getPathKey() {
        return pathKey;
    }

    public void setPathKey(String pathKey) {
        this.pathKey = pathKey;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPasswordEnc() {
        return dbPasswordEnc;
    }

    public void setDbPasswordEnc(String dbPasswordEnc) {
        this.dbPasswordEnc = dbPasswordEnc;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}