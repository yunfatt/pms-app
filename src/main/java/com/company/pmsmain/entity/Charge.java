package com.company.pmsmain.entity;

import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.math.BigDecimal;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "tenant")
@Table(name = "charges")
@Entity
public class Charge {
    @Column(name = "uid", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    @Column(name = "chargetype")
    private Character chargetype;

    @InstanceName
    @Column(name = "description", length = 80)
    private String description;

    @Column(name = "minamt", precision = 8, scale = 2)
    private BigDecimal minamt;

    @Column(name = "minqty")
    private Integer minqty;

    @Column(name = "monthdue")
    private Short monthdue;

    @Column(name = "period")
    private Short period;

    @Column(name = "phaseno", length = 6)
    private String phaseno;

    @Column(name = "price", precision = 14, scale = 4)
    private BigDecimal price;

    @Column(name = "price2", precision = 14, scale = 4)
    private BigDecimal price2;

    @Column(name = "price3", precision = 14, scale = 4)
    private BigDecimal price3;

    @Column(name = "price4", precision = 14, scale = 4)
    private BigDecimal price4;

    @Column(name = "price5", precision = 14, scale = 4)
    private BigDecimal price5;

    @Column(name = "readingtype", length = 6)
    private String readingtype;

    @Column(name = "taxcodeid")
    private Integer taxcodeid;

    @Column(name = "taxinvtype")
    private Integer taxinvtype;

    @Column(name = "trxncode", length = 10)
    private String trxncode;

    @Column(name = "unit", precision = 8, scale = 2)
    private BigDecimal unit;

    @Column(name = "unit2", precision = 8, scale = 2)
    private BigDecimal unit2;

    @Column(name = "unit3", precision = 8, scale = 2)
    private BigDecimal unit3;

    @Column(name = "unit4", precision = 8, scale = 2)
    private BigDecimal unit4;

    @Column(name = "unit5", precision = 8, scale = 2)
    private BigDecimal unit5;

    @Column(name = "usedefaulttaxcode")
    private Short usedefaulttaxcode;

    public Short getUsedefaulttaxcode() {
        return usedefaulttaxcode;
    }

    public void setUsedefaulttaxcode(Short usedefaulttaxcode) {
        this.usedefaulttaxcode = usedefaulttaxcode;
    }

    public BigDecimal getUnit5() {
        return unit5;
    }

    public void setUnit5(BigDecimal unit5) {
        this.unit5 = unit5;
    }

    public BigDecimal getUnit4() {
        return unit4;
    }

    public void setUnit4(BigDecimal unit4) {
        this.unit4 = unit4;
    }

    public BigDecimal getUnit3() {
        return unit3;
    }

    public void setUnit3(BigDecimal unit3) {
        this.unit3 = unit3;
    }

    public BigDecimal getUnit2() {
        return unit2;
    }

    public void setUnit2(BigDecimal unit2) {
        this.unit2 = unit2;
    }

    public BigDecimal getUnit() {
        return unit;
    }

    public void setUnit(BigDecimal unit) {
        this.unit = unit;
    }

    public String getTrxncode() {
        return trxncode;
    }

    public void setTrxncode(String trxncode) {
        this.trxncode = trxncode;
    }

    public Integer getTaxinvtype() {
        return taxinvtype;
    }

    public void setTaxinvtype(Integer taxinvtype) {
        this.taxinvtype = taxinvtype;
    }

    public Integer getTaxcodeid() {
        return taxcodeid;
    }

    public void setTaxcodeid(Integer taxcodeid) {
        this.taxcodeid = taxcodeid;
    }

    public String getReadingtype() {
        return readingtype;
    }

    public void setReadingtype(String readingtype) {
        this.readingtype = readingtype;
    }

    public BigDecimal getPrice5() {
        return price5;
    }

    public void setPrice5(BigDecimal price5) {
        this.price5 = price5;
    }

    public BigDecimal getPrice4() {
        return price4;
    }

    public void setPrice4(BigDecimal price4) {
        this.price4 = price4;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPhaseno() {
        return phaseno;
    }

    public void setPhaseno(String phaseno) {
        this.phaseno = phaseno;
    }

    public Short getPeriod() {
        return period;
    }

    public void setPeriod(Short period) {
        this.period = period;
    }

    public Short getMonthdue() {
        return monthdue;
    }

    public void setMonthdue(Short monthdue) {
        this.monthdue = monthdue;
    }

    public Integer getMinqty() {
        return minqty;
    }

    public void setMinqty(Integer minqty) {
        this.minqty = minqty;
    }

    public BigDecimal getMinamt() {
        return minamt;
    }

    public void setMinamt(BigDecimal minamt) {
        this.minamt = minamt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Character getChargetype() {
        return chargetype;
    }

    public void setChargetype(Character chargetype) {
        this.chargetype = chargetype;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

}