package com.company.pmsmain.entity;

import com.company.pmsmain.entity.key.BankCompKey;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "tenant")
@Table(name = "bank")
@Entity
public class Bank {
    @EmbeddedId
    private BankCompKey id;

    @Column(name = "accno", length = 12)
    private String accno;

    @Column(name = "allowbackdate")
    private Short allowbackdate;

    @Column(name = "aptrxncode", length = 10)
    private String aptrxncode;

    @Column(name = "aptrxncodercpt", length = 10)
    private String aptrxncodercpt;

    @Column(name = "artrxncode", length = 10)
    private String artrxncode;

    @Column(name = "artrxncodedep", length = 10)
    private String artrxncodedep;

    @Column(name = "artrxncodepv", length = 10)
    private String artrxncodepv;

    @Column(name = "artrxncoderc", length = 10)
    private String artrxncoderc;

    @Column(name = "bankaccno", length = 30)
    private String bankaccno;

    @Column(name = "bcaccno", length = 12)
    private String bcaccno;

    @Column(name = "bcprjno", length = 8)
    private String bcprjno;

    @Column(name = "chargesaccno", length = 12)
    private String chargesaccno;

    @Column(name = "docno")
    private Integer docno;

    @Column(name = "jompaypayerbankid", length = 10)
    private String jompaypayerbankid;

    @InstanceName
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "payauto")
    private Short payauto;

    @Column(name = "payautoformat")
    private Short payautoformat;

    @Column(name = "payno")
    private Integer payno;

    @Column(name = "payprefix", length = 2)
    private String payprefix;

    @Column(name = "paysuppprefix")
    private Short paysuppprefix;

    @Column(name = "prjno", length = 8)
    private String prjno;

    @Column(name = "rcptauto")
    private Short rcptauto;

    @Column(name = "rcptautoformat")
    private Short rcptautoformat;

    @Column(name = "rcptno")
    private Integer rcptno;

    @Column(name = "rcptprefix", length = 2)
    private String rcptprefix;

    @Column(name = "rcptsuppprefix")
    private Short rcptsuppprefix;

    @Column(name = "reconcile")
    private Short reconcile;

    @Column(name = "securitygroup")
    private Integer securitygroup;

    @Column(name = "slip")
    private Short slip;

    public Short getSlip() {
        return slip;
    }

    public void setSlip(Short slip) {
        this.slip = slip;
    }

    public Integer getSecuritygroup() {
        return securitygroup;
    }

    public void setSecuritygroup(Integer securitygroup) {
        this.securitygroup = securitygroup;
    }

    public Short getReconcile() {
        return reconcile;
    }

    public void setReconcile(Short reconcile) {
        this.reconcile = reconcile;
    }

    public Short getRcptsuppprefix() {
        return rcptsuppprefix;
    }

    public void setRcptsuppprefix(Short rcptsuppprefix) {
        this.rcptsuppprefix = rcptsuppprefix;
    }

    public String getRcptprefix() {
        return rcptprefix;
    }

    public void setRcptprefix(String rcptprefix) {
        this.rcptprefix = rcptprefix;
    }

    public Integer getRcptno() {
        return rcptno;
    }

    public void setRcptno(Integer rcptno) {
        this.rcptno = rcptno;
    }

    public Short getRcptautoformat() {
        return rcptautoformat;
    }

    public void setRcptautoformat(Short rcptautoformat) {
        this.rcptautoformat = rcptautoformat;
    }

    public Short getRcptauto() {
        return rcptauto;
    }

    public void setRcptauto(Short rcptauto) {
        this.rcptauto = rcptauto;
    }

    public String getPrjno() {
        return prjno;
    }

    public void setPrjno(String prjno) {
        this.prjno = prjno;
    }

    public Short getPaysuppprefix() {
        return paysuppprefix;
    }

    public void setPaysuppprefix(Short paysuppprefix) {
        this.paysuppprefix = paysuppprefix;
    }

    public String getPayprefix() {
        return payprefix;
    }

    public void setPayprefix(String payprefix) {
        this.payprefix = payprefix;
    }

    public Integer getPayno() {
        return payno;
    }

    public void setPayno(Integer payno) {
        this.payno = payno;
    }

    public Short getPayautoformat() {
        return payautoformat;
    }

    public void setPayautoformat(Short payautoformat) {
        this.payautoformat = payautoformat;
    }

    public Short getPayauto() {
        return payauto;
    }

    public void setPayauto(Short payauto) {
        this.payauto = payauto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJompaypayerbankid() {
        return jompaypayerbankid;
    }

    public void setJompaypayerbankid(String jompaypayerbankid) {
        this.jompaypayerbankid = jompaypayerbankid;
    }

    public Integer getDocno() {
        return docno;
    }

    public void setDocno(Integer docno) {
        this.docno = docno;
    }

    public String getChargesaccno() {
        return chargesaccno;
    }

    public void setChargesaccno(String chargesaccno) {
        this.chargesaccno = chargesaccno;
    }

    public String getBcprjno() {
        return bcprjno;
    }

    public void setBcprjno(String bcprjno) {
        this.bcprjno = bcprjno;
    }

    public String getBcaccno() {
        return bcaccno;
    }

    public void setBcaccno(String bcaccno) {
        this.bcaccno = bcaccno;
    }

    public String getBankaccno() {
        return bankaccno;
    }

    public void setBankaccno(String bankaccno) {
        this.bankaccno = bankaccno;
    }

    public String getArtrxncoderc() {
        return artrxncoderc;
    }

    public void setArtrxncoderc(String artrxncoderc) {
        this.artrxncoderc = artrxncoderc;
    }

    public String getArtrxncodepv() {
        return artrxncodepv;
    }

    public void setArtrxncodepv(String artrxncodepv) {
        this.artrxncodepv = artrxncodepv;
    }

    public String getArtrxncodedep() {
        return artrxncodedep;
    }

    public void setArtrxncodedep(String artrxncodedep) {
        this.artrxncodedep = artrxncodedep;
    }

    public String getArtrxncode() {
        return artrxncode;
    }

    public void setArtrxncode(String artrxncode) {
        this.artrxncode = artrxncode;
    }

    public String getAptrxncodercpt() {
        return aptrxncodercpt;
    }

    public void setAptrxncodercpt(String aptrxncodercpt) {
        this.aptrxncodercpt = aptrxncodercpt;
    }

    public String getAptrxncode() {
        return aptrxncode;
    }

    public void setAptrxncode(String aptrxncode) {
        this.aptrxncode = aptrxncode;
    }

    public Short getAllowbackdate() {
        return allowbackdate;
    }

    public void setAllowbackdate(Short allowbackdate) {
        this.allowbackdate = allowbackdate;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public BankCompKey getId() {
        return id;
    }

    public void setId(BankCompKey id) {
        this.id = id;
    }

}