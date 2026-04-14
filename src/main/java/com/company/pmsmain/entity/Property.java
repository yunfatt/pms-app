package com.company.pmsmain.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "tenant")
@Table(name = "property")
@Entity
public class Property {
    @Column(name = "propertyno", nullable = false, length = 12)
    @Id
    private String propertyno;

    @Column(name = "addr1", length = 50)
    private String addr1;

    @Column(name = "addr2", length = 50)
    private String addr2;

    @Column(name = "addr3", length = 50)
    private String addr3;

    @Column(name = "addr4", length = 50)
    private String addr4;

    @Column(name = "blockno", length = 6)
    private String blockno;

    @Column(name = "buildingno", length = 6)
    private String buildingno;

    @Column(name = "buildup", precision = 10, scale = 2)
    private BigDecimal buildup;

    @Column(name = "catcode", length = 3)
    private String catcode;

    @Column(name = "commercial")
    private Short commercial;

    @Temporal(TemporalType.DATE)
    @Column(name = "confletterrcvd")
    private Date confletterrcvd;

    @Temporal(TemporalType.DATE)
    @Column(name = "consentdate")
    private Date consentdate;

    @Column(name = "ctos")
    private Short ctos;

    @Column(name = "electtariff")
    private Character electtariff;

    @Column(name = "emeterno", length = 20)
    private String emeterno;

    @Column(name = "ereading1", precision = 10, scale = 0)
    private BigDecimal ereading1;

    @Column(name = "gmeterno", length = 20)
    private String gmeterno;

    @Column(name = "greading1", precision = 10, scale = 0)
    private BigDecimal greading1;

    @Column(name = "legalaction")
    private Short legalaction;

    @Column(name = "levelno", length = 20)
    private String levelno;

    @Temporal(TemporalType.DATE)
    @Column(name = "motpresented")
    private Date motpresented;

    @Column(name = "motpresentno", length = 20)
    private String motpresentno;

    @Temporal(TemporalType.DATE)
    @Column(name = "motreceived")
    private Date motreceived;

    @Column(name = "occupied")
    private Short occupied;

    @Temporal(TemporalType.DATE)
    @Column(name = "origstratatitlereld")
    private Date origstratatitlereld;

    @Column(name = "ownerno")
    private Short ownerno;

    @Column(name = "phaseno", length = 6)
    private String phaseno;

    @Column(name = "propertystatus")
    private Character propertystatus;

    @Column(name = "rate_sc", precision = 10, scale = 4)
    private BigDecimal rateSc;

    @Column(name = "rate_sf", precision = 10, scale = 4)
    private BigDecimal rateSf;

    @Column(name = "renovated")
    private Short renovated;

    @Column(name = "renovateremark", length = 100)
    private String renovateremark;

    @Column(name = "residentstatus")
    private Integer residentstatus;

    @Column(name = "sellingprice", precision = 12, scale = 2)
    private BigDecimal sellingprice;

    @Column(name = "shareunit", precision = 10, scale = 2)
    private BigDecimal shareunit;

    @Column(name = "strataremarks", length = 30)
    private String strataremarks;

    @Column(name = "stratatitleno", length = 20)
    private String stratatitleno;

    @Column(name = "tenantno")
    private Short tenantno;

    @Column(name = "unitno", length = 12)
    private String unitno;

    @Column(name = "unituid", unique = true)
    private Integer unituid;

    @Column(name = "wmeterno", length = 20)
    private String wmeterno;

    @Column(name = "wreading1", precision = 10, scale = 0)
    private BigDecimal wreading1;

    public BigDecimal getWreading1() {
        return wreading1;
    }

    public void setWreading1(BigDecimal wreading1) {
        this.wreading1 = wreading1;
    }

    public String getWmeterno() {
        return wmeterno;
    }

    public void setWmeterno(String wmeterno) {
        this.wmeterno = wmeterno;
    }

    public Integer getUnituid() {
        return unituid;
    }

    public void setUnituid(Integer unituid) {
        this.unituid = unituid;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public Short getTenantno() {
        return tenantno;
    }

    public void setTenantno(Short tenantno) {
        this.tenantno = tenantno;
    }

    public String getStratatitleno() {
        return stratatitleno;
    }

    public void setStratatitleno(String stratatitleno) {
        this.stratatitleno = stratatitleno;
    }

    public String getStrataremarks() {
        return strataremarks;
    }

    public void setStrataremarks(String strataremarks) {
        this.strataremarks = strataremarks;
    }

    public BigDecimal getShareunit() {
        return shareunit;
    }

    public void setShareunit(BigDecimal shareunit) {
        this.shareunit = shareunit;
    }

    public BigDecimal getSellingprice() {
        return sellingprice;
    }

    public void setSellingprice(BigDecimal sellingprice) {
        this.sellingprice = sellingprice;
    }

    public Integer getResidentstatus() {
        return residentstatus;
    }

    public void setResidentstatus(Integer residentstatus) {
        this.residentstatus = residentstatus;
    }

    public String getRenovateremark() {
        return renovateremark;
    }

    public void setRenovateremark(String renovateremark) {
        this.renovateremark = renovateremark;
    }

    public Short getRenovated() {
        return renovated;
    }

    public void setRenovated(Short renovated) {
        this.renovated = renovated;
    }

    public BigDecimal getRateSf() {
        return rateSf;
    }

    public void setRateSf(BigDecimal rateSf) {
        this.rateSf = rateSf;
    }

    public BigDecimal getRateSc() {
        return rateSc;
    }

    public void setRateSc(BigDecimal rateSc) {
        this.rateSc = rateSc;
    }

    public Character getPropertystatus() {
        return propertystatus;
    }

    public void setPropertystatus(Character propertystatus) {
        this.propertystatus = propertystatus;
    }

    public String getPhaseno() {
        return phaseno;
    }

    public void setPhaseno(String phaseno) {
        this.phaseno = phaseno;
    }

    public Short getOwnerno() {
        return ownerno;
    }

    public void setOwnerno(Short ownerno) {
        this.ownerno = ownerno;
    }

    public Date getOrigstratatitlereld() {
        return origstratatitlereld;
    }

    public void setOrigstratatitlereld(Date origstratatitlereld) {
        this.origstratatitlereld = origstratatitlereld;
    }

    public Short getOccupied() {
        return occupied;
    }

    public void setOccupied(Short occupied) {
        this.occupied = occupied;
    }

    public Date getMotreceived() {
        return motreceived;
    }

    public void setMotreceived(Date motreceived) {
        this.motreceived = motreceived;
    }

    public String getMotpresentno() {
        return motpresentno;
    }

    public void setMotpresentno(String motpresentno) {
        this.motpresentno = motpresentno;
    }

    public Date getMotpresented() {
        return motpresented;
    }

    public void setMotpresented(Date motpresented) {
        this.motpresented = motpresented;
    }

    public String getLevelno() {
        return levelno;
    }

    public void setLevelno(String levelno) {
        this.levelno = levelno;
    }

    public Short getLegalaction() {
        return legalaction;
    }

    public void setLegalaction(Short legalaction) {
        this.legalaction = legalaction;
    }

    public BigDecimal getGreading1() {
        return greading1;
    }

    public void setGreading1(BigDecimal greading1) {
        this.greading1 = greading1;
    }

    public String getGmeterno() {
        return gmeterno;
    }

    public void setGmeterno(String gmeterno) {
        this.gmeterno = gmeterno;
    }

    public BigDecimal getEreading1() {
        return ereading1;
    }

    public void setEreading1(BigDecimal ereading1) {
        this.ereading1 = ereading1;
    }

    public String getEmeterno() {
        return emeterno;
    }

    public void setEmeterno(String emeterno) {
        this.emeterno = emeterno;
    }

    public Character getElecttariff() {
        return electtariff;
    }

    public void setElecttariff(Character electtariff) {
        this.electtariff = electtariff;
    }

    public Short getCtos() {
        return ctos;
    }

    public void setCtos(Short ctos) {
        this.ctos = ctos;
    }

    public Date getConsentdate() {
        return consentdate;
    }

    public void setConsentdate(Date consentdate) {
        this.consentdate = consentdate;
    }

    public Date getConfletterrcvd() {
        return confletterrcvd;
    }

    public void setConfletterrcvd(Date confletterrcvd) {
        this.confletterrcvd = confletterrcvd;
    }

    public Short getCommercial() {
        return commercial;
    }

    public void setCommercial(Short commercial) {
        this.commercial = commercial;
    }

    public String getCatcode() {
        return catcode;
    }

    public void setCatcode(String catcode) {
        this.catcode = catcode;
    }

    public BigDecimal getBuildup() {
        return buildup;
    }

    public void setBuildup(BigDecimal buildup) {
        this.buildup = buildup;
    }

    public String getBuildingno() {
        return buildingno;
    }

    public void setBuildingno(String buildingno) {
        this.buildingno = buildingno;
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }

    public String getAddr4() {
        return addr4;
    }

    public void setAddr4(String addr4) {
        this.addr4 = addr4;
    }

    public String getAddr3() {
        return addr3;
    }

    public void setAddr3(String addr3) {
        this.addr3 = addr3;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getPropertyno() {
        return propertyno;
    }

    public void setPropertyno(String propertyno) {
        this.propertyno = propertyno;
    }

}