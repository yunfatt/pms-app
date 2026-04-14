package com.company.pmsmain.entity;

import com.company.pmsmain.entity.key.InvdtlCompKey;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "tenant")
@Table(name = "invdtl")
@Entity
public class Invdtl {
    @EmbeddedId
    private InvdtlCompKey id;

    @Column(name = "amount", precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "chargeid")
    private Integer chargeid;

    @Column(name = "chargeno", length = 10)
    private String chargeno;

    @Column(name = "compno", length = 2)
    private String compno;

    @Temporal(TemporalType.DATE)
    @Column(name = "date1")
    private Date date1;

    @Temporal(TemporalType.DATE)
    @Column(name = "date2")
    private Date date2;

    @InstanceName
    @Column(name = "description", length = 80)
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "duedate")
    private Date duedate;

    @Column(name = "echargeid")
    private Integer echargeid;

    @Column(name = "gstamt", precision = 10, scale = 2)
    private BigDecimal gstamt;

    @Column(name = "gstpayable", precision = 14, scale = 2)
    private BigDecimal gstpayable;

    @Column(name = "gstrate", precision = 4, scale = 0)
    private BigDecimal gstrate;

    @Temporal(TemporalType.DATE)
    @Column(name = "gsttaxpointdate")
    private Date gsttaxpointdate;

    @Column(name = "icptamt", precision = 14, scale = 2)
    private BigDecimal icptamt;

    @Column(name = "icptqty", precision = 10, scale = 2)
    private BigDecimal icptqty;

    @Column(name = "icptqty2", precision = 10, scale = 2)
    private BigDecimal icptqty2;

    @Column(name = "icptqty3", precision = 10, scale = 2)
    private BigDecimal icptqty3;

    @Column(name = "icptqty4", precision = 10, scale = 2)
    private BigDecimal icptqty4;

    @Column(name = "icptqty5", precision = 10, scale = 2)
    private BigDecimal icptqty5;

    @Column(name = "icptqty6", precision = 10, scale = 2)
    private BigDecimal icptqty6;

    @Column(name = "icptrate", precision = 8, scale = 4)
    private BigDecimal icptrate;

    @Column(name = "icptrate2", precision = 8, scale = 4)
    private BigDecimal icptrate2;

    @Column(name = "icptrate3", precision = 8, scale = 4)
    private BigDecimal icptrate3;

    @Column(name = "icptrate4", precision = 8, scale = 4)
    private BigDecimal icptrate4;

    @Column(name = "icptrate5", precision = 8, scale = 4)
    private BigDecimal icptrate5;

    @Column(name = "icptrate6", precision = 8, scale = 4)
    private BigDecimal icptrate6;

    @Column(name = "maxdemand", precision = 8, scale = 2)
    private BigDecimal maxdemand;

    @Column(name = "netamt", precision = 14, scale = 2)
    private BigDecimal netamt;

    @Column(name = "period")
    private Integer period;

    @Column(name = "price", precision = 12, scale = 4)
    private BigDecimal price;

    @Column(name = "price2", precision = 12, scale = 4)
    private BigDecimal price2;

    @Column(name = "price3", precision = 12, scale = 4)
    private BigDecimal price3;

    @Column(name = "price4", precision = 12, scale = 4)
    private BigDecimal price4;

    @Column(name = "price5", precision = 12, scale = 4)
    private BigDecimal price5;

    @Column(name = "price6", precision = 12, scale = 4)
    private BigDecimal price6;

    @Column(name = "qty", precision = 12, scale = 2)
    private BigDecimal qty;

    @Column(name = "qty2", precision = 12, scale = 2)
    private BigDecimal qty2;

    @Column(name = "qty3", precision = 12, scale = 2)
    private BigDecimal qty3;

    @Column(name = "qty4", precision = 12, scale = 2)
    private BigDecimal qty4;

    @Column(name = "qty5", precision = 12, scale = 2)
    private BigDecimal qty5;

    @Column(name = "qty6", precision = 12, scale = 2)
    private BigDecimal qty6;

    @Column(name = "reading1")
    private Integer reading1;

    @Column(name = "reading2")
    private Integer reading2;

    @Column(name = "reamt", precision = 8, scale = 2)
    private BigDecimal reamt;

    @Column(name = "rerate", precision = 8, scale = 4)
    private BigDecimal rerate;

    @Column(name = "taxcodeid")
    private Integer taxcodeid;

    @Column(name = "unit", length = 8)
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getTaxcodeid() {
        return taxcodeid;
    }

    public void setTaxcodeid(Integer taxcodeid) {
        this.taxcodeid = taxcodeid;
    }

    public BigDecimal getRerate() {
        return rerate;
    }

    public void setRerate(BigDecimal rerate) {
        this.rerate = rerate;
    }

    public BigDecimal getReamt() {
        return reamt;
    }

    public void setReamt(BigDecimal reamt) {
        this.reamt = reamt;
    }

    public Integer getReading2() {
        return reading2;
    }

    public void setReading2(Integer reading2) {
        this.reading2 = reading2;
    }

    public Integer getReading1() {
        return reading1;
    }

    public void setReading1(Integer reading1) {
        this.reading1 = reading1;
    }

    public BigDecimal getQty6() {
        return qty6;
    }

    public void setQty6(BigDecimal qty6) {
        this.qty6 = qty6;
    }

    public BigDecimal getQty5() {
        return qty5;
    }

    public void setQty5(BigDecimal qty5) {
        this.qty5 = qty5;
    }

    public BigDecimal getQty4() {
        return qty4;
    }

    public void setQty4(BigDecimal qty4) {
        this.qty4 = qty4;
    }

    public BigDecimal getQty3() {
        return qty3;
    }

    public void setQty3(BigDecimal qty3) {
        this.qty3 = qty3;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice6() {
        return price6;
    }

    public void setPrice6(BigDecimal price6) {
        this.price6 = price6;
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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getNetamt() {
        return netamt;
    }

    public void setNetamt(BigDecimal netamt) {
        this.netamt = netamt;
    }

    public BigDecimal getMaxdemand() {
        return maxdemand;
    }

    public void setMaxdemand(BigDecimal maxdemand) {
        this.maxdemand = maxdemand;
    }

    public BigDecimal getIcptrate6() {
        return icptrate6;
    }

    public void setIcptrate6(BigDecimal icptrate6) {
        this.icptrate6 = icptrate6;
    }

    public BigDecimal getIcptrate5() {
        return icptrate5;
    }

    public void setIcptrate5(BigDecimal icptrate5) {
        this.icptrate5 = icptrate5;
    }

    public BigDecimal getIcptrate4() {
        return icptrate4;
    }

    public void setIcptrate4(BigDecimal icptrate4) {
        this.icptrate4 = icptrate4;
    }

    public BigDecimal getIcptrate3() {
        return icptrate3;
    }

    public void setIcptrate3(BigDecimal icptrate3) {
        this.icptrate3 = icptrate3;
    }

    public BigDecimal getIcptrate2() {
        return icptrate2;
    }

    public void setIcptrate2(BigDecimal icptrate2) {
        this.icptrate2 = icptrate2;
    }

    public BigDecimal getIcptrate() {
        return icptrate;
    }

    public void setIcptrate(BigDecimal icptrate) {
        this.icptrate = icptrate;
    }

    public BigDecimal getIcptqty6() {
        return icptqty6;
    }

    public void setIcptqty6(BigDecimal icptqty6) {
        this.icptqty6 = icptqty6;
    }

    public BigDecimal getIcptqty5() {
        return icptqty5;
    }

    public void setIcptqty5(BigDecimal icptqty5) {
        this.icptqty5 = icptqty5;
    }

    public BigDecimal getIcptqty4() {
        return icptqty4;
    }

    public void setIcptqty4(BigDecimal icptqty4) {
        this.icptqty4 = icptqty4;
    }

    public BigDecimal getIcptqty3() {
        return icptqty3;
    }

    public void setIcptqty3(BigDecimal icptqty3) {
        this.icptqty3 = icptqty3;
    }

    public BigDecimal getIcptqty2() {
        return icptqty2;
    }

    public void setIcptqty2(BigDecimal icptqty2) {
        this.icptqty2 = icptqty2;
    }

    public BigDecimal getIcptqty() {
        return icptqty;
    }

    public void setIcptqty(BigDecimal icptqty) {
        this.icptqty = icptqty;
    }

    public BigDecimal getIcptamt() {
        return icptamt;
    }

    public void setIcptamt(BigDecimal icptamt) {
        this.icptamt = icptamt;
    }

    public Date getGsttaxpointdate() {
        return gsttaxpointdate;
    }

    public void setGsttaxpointdate(Date gsttaxpointdate) {
        this.gsttaxpointdate = gsttaxpointdate;
    }

    public BigDecimal getGstrate() {
        return gstrate;
    }

    public void setGstrate(BigDecimal gstrate) {
        this.gstrate = gstrate;
    }

    public BigDecimal getGstpayable() {
        return gstpayable;
    }

    public void setGstpayable(BigDecimal gstpayable) {
        this.gstpayable = gstpayable;
    }

    public BigDecimal getGstamt() {
        return gstamt;
    }

    public void setGstamt(BigDecimal gstamt) {
        this.gstamt = gstamt;
    }

    public Integer getEchargeid() {
        return echargeid;
    }

    public void setEchargeid(Integer echargeid) {
        this.echargeid = echargeid;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public String getCompno() {
        return compno;
    }

    public void setCompno(String compno) {
        this.compno = compno;
    }

    public String getChargeno() {
        return chargeno;
    }

    public void setChargeno(String chargeno) {
        this.chargeno = chargeno;
    }

    public Integer getChargeid() {
        return chargeid;
    }

    public void setChargeid(Integer chargeid) {
        this.chargeid = chargeid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public InvdtlCompKey getId() {
        return id;
    }

    public void setId(InvdtlCompKey id) {
        this.id = id;
    }

}