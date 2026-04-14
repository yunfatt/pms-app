package com.company.pmsmain.entity;

import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "tenant")
@Table(name = "ardoc")
@Entity
public class Ardoc {
    @Column(name = "trxncode", nullable = false, length = 10)
    @Id
    private String trxncode;

    @Column(name = "accno", length = 12)
    private String accno;

    @Column(name = "apaccno", length = 12)
    private String apaccno;

    @Column(name = "autotransfer")
    private Short autotransfer;

    @Column(name = "billto")
    private Character billto;

    @Column(name = "chargeinterest")
    private Short chargeinterest;

    @Column(name = "classification", length = 3)
    private String classification;

    @Column(name = "cncode", length = 10)
    private String cncode;

    @Column(name = "deposit")
    private Short deposit;

    @InstanceName
    @Column(name = "description", length = 50)
    private String description;

    @Column(name = "intrate", precision = 8, scale = 2)
    private BigDecimal intrate;

    @Column(name = "longterm")
    private Short longterm;

    @Column(name = "prjno", length = 8)
    private String prjno;

    @Column(name = "ptype")
    private Character ptype;

    @Column(name = "readingfrom", length = 6)
    private String readingfrom;

    @Column(name = "refundcode", length = 10)
    private String refundcode;

    @Column(name = "sinkingfund")
    private Short sinkingfund;

    @Column(name = "taxcodeid")
    private Integer taxcodeid;

    @Column(name = "term")
    private Short term;

    @Column(name = "ttype")
    private Character ttype;

    @Column(name = "utility")
    private Short utility;

    public Short getUtility() {
        return utility;
    }

    public void setUtility(Short utility) {
        this.utility = utility;
    }

    public Character getTtype() {
        return ttype;
    }

    public void setTtype(Character ttype) {
        this.ttype = ttype;
    }

    public Short getTerm() {
        return term;
    }

    public void setTerm(Short term) {
        this.term = term;
    }

    public Integer getTaxcodeid() {
        return taxcodeid;
    }

    public void setTaxcodeid(Integer taxcodeid) {
        this.taxcodeid = taxcodeid;
    }

    public Short getSinkingfund() {
        return sinkingfund;
    }

    public void setSinkingfund(Short sinkingfund) {
        this.sinkingfund = sinkingfund;
    }

    public String getRefundcode() {
        return refundcode;
    }

    public void setRefundcode(String refundcode) {
        this.refundcode = refundcode;
    }

    public String getReadingfrom() {
        return readingfrom;
    }

    public void setReadingfrom(String readingfrom) {
        this.readingfrom = readingfrom;
    }

    public Character getPtype() {
        return ptype;
    }

    public void setPtype(Character ptype) {
        this.ptype = ptype;
    }

    public String getPrjno() {
        return prjno;
    }

    public void setPrjno(String prjno) {
        this.prjno = prjno;
    }

    public Short getLongterm() {
        return longterm;
    }

    public void setLongterm(Short longterm) {
        this.longterm = longterm;
    }

    public BigDecimal getIntrate() {
        return intrate;
    }

    public void setIntrate(BigDecimal intrate) {
        this.intrate = intrate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getDeposit() {
        return deposit;
    }

    public void setDeposit(Short deposit) {
        this.deposit = deposit;
    }

    public String getCncode() {
        return cncode;
    }

    public void setCncode(String cncode) {
        this.cncode = cncode;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Short getChargeinterest() {
        return chargeinterest;
    }

    public void setChargeinterest(Short chargeinterest) {
        this.chargeinterest = chargeinterest;
    }

    public Character getBillto() {
        return billto;
    }

    public void setBillto(Character billto) {
        this.billto = billto;
    }

    public Short getAutotransfer() {
        return autotransfer;
    }

    public void setAutotransfer(Short autotransfer) {
        this.autotransfer = autotransfer;
    }

    public String getApaccno() {
        return apaccno;
    }

    public void setApaccno(String apaccno) {
        this.apaccno = apaccno;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getTrxncode() {
        return trxncode;
    }

    public void setTrxncode(String trxncode) {
        this.trxncode = trxncode;
    }

}