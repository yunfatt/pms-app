package com.company.pmsmain.entity;

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
@Table(name = "phase")
@Entity
public class Phase {
    @Column(name = "phaseno", nullable = false, length = 6)
    @Id
    private String phaseno;

    @Column(name = "area", length = 10)
    private String area;

    @Column(name = "bank", length = 8)
    private String bank;

    @Column(name = "bankid", length = 2)
    private String bankid;

    @Column(name = "bankid2", length = 2)
    private String bankid2;

    @Column(name = "bankid3", length = 2)
    private String bankid3;

    @Column(name = "bankid4", length = 2)
    private String bankid4;

    @Column(name = "billercode")
    private Integer billercode;

    @Column(name = "cimb_id", precision = 4, scale = 0)
    private BigDecimal cimb;

    @Column(name = "cimb_name", length = 15)
    private String cimbName;

    @Column(name = "cimb_regno", precision = 10, scale = 0)
    private BigDecimal cimbRegno;

    @Column(name = "corpabbr", length = 3)
    private String corpabbr;

    @Column(name = "custaccno", length = 8)
    private String custaccno;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateend")
    private Date dateend;

    @Temporal(TemporalType.DATE)
    @Column(name = "datestart")
    private Date datestart;

    @Column(name = "district", length = 20)
    private String district;

    @Column(name = "institutionid", length = 4)
    private String institutionid;

    @Column(name = "institutionname", length = 30)
    private String institutionname;

    @Column(name = "lotno", length = 15)
    private String lotno;

    @Column(name = "merchant", length = 15)
    private String merchant;

    @Column(name = "merchant2", length = 15)
    private String merchant2;

    @Column(name = "merchant3", length = 15)
    private String merchant3;

    @Column(name = "merchant4", length = 15)
    private String merchant4;

    @Column(name = "parcelno", length = 30)
    private String parcelno;

    @Column(name = "parcelshare", precision = 6, scale = 0)
    private BigDecimal parcelshare;

    @Column(name = "pbprod", length = 3)
    private String pbprod;

    @Column(name = "pbtransfertype")
    private Character pbtransfertype;

    @Column(name = "phasename", length = 50)
    private String phasename;

    @Column(name = "praddress1", length = 40)
    private String praddress1;

    @Column(name = "praddress2", length = 40)
    private String praddress2;

    @Column(name = "praddress3", length = 40)
    private String praddress3;

    @Column(name = "praddress4", length = 40)
    private String praddress4;

    @Column(name = "prjno", length = 8)
    private String prjno;

    @Column(name = "proprietorname", length = 80)
    private String proprietorname;

    @Column(name = "registerid", length = 9)
    private String registerid;

    @Column(name = "registerid2", length = 9)
    private String registerid2;

    @Column(name = "registerid3", length = 9)
    private String registerid3;

    @Column(name = "registerid4", length = 9)
    private String registerid4;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "sinkaccno", length = 8)
    private String sinkaccno;

    @Column(name = "sinkcode", length = 6)
    private String sinkcode;

    @InstanceName
    @Column(name = "title", length = 20)
    private String title;

    @Column(name = "town", length = 20)
    private String town;

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinkcode() {
        return sinkcode;
    }

    public void setSinkcode(String sinkcode) {
        this.sinkcode = sinkcode;
    }

    public String getSinkaccno() {
        return sinkaccno;
    }

    public void setSinkaccno(String sinkaccno) {
        this.sinkaccno = sinkaccno;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRegisterid4() {
        return registerid4;
    }

    public void setRegisterid4(String registerid4) {
        this.registerid4 = registerid4;
    }

    public String getRegisterid3() {
        return registerid3;
    }

    public void setRegisterid3(String registerid3) {
        this.registerid3 = registerid3;
    }

    public String getRegisterid2() {
        return registerid2;
    }

    public void setRegisterid2(String registerid2) {
        this.registerid2 = registerid2;
    }

    public String getRegisterid() {
        return registerid;
    }

    public void setRegisterid(String registerid) {
        this.registerid = registerid;
    }

    public String getProprietorname() {
        return proprietorname;
    }

    public void setProprietorname(String proprietorname) {
        this.proprietorname = proprietorname;
    }

    public String getPrjno() {
        return prjno;
    }

    public void setPrjno(String prjno) {
        this.prjno = prjno;
    }

    public String getPraddress4() {
        return praddress4;
    }

    public void setPraddress4(String praddress4) {
        this.praddress4 = praddress4;
    }

    public String getPraddress3() {
        return praddress3;
    }

    public void setPraddress3(String praddress3) {
        this.praddress3 = praddress3;
    }

    public String getPraddress2() {
        return praddress2;
    }

    public void setPraddress2(String praddress2) {
        this.praddress2 = praddress2;
    }

    public String getPraddress1() {
        return praddress1;
    }

    public void setPraddress1(String praddress1) {
        this.praddress1 = praddress1;
    }

    public String getPhasename() {
        return phasename;
    }

    public void setPhasename(String phasename) {
        this.phasename = phasename;
    }

    public Character getPbtransfertype() {
        return pbtransfertype;
    }

    public void setPbtransfertype(Character pbtransfertype) {
        this.pbtransfertype = pbtransfertype;
    }

    public String getPbprod() {
        return pbprod;
    }

    public void setPbprod(String pbprod) {
        this.pbprod = pbprod;
    }

    public BigDecimal getParcelshare() {
        return parcelshare;
    }

    public void setParcelshare(BigDecimal parcelshare) {
        this.parcelshare = parcelshare;
    }

    public String getParcelno() {
        return parcelno;
    }

    public void setParcelno(String parcelno) {
        this.parcelno = parcelno;
    }

    public String getMerchant4() {
        return merchant4;
    }

    public void setMerchant4(String merchant4) {
        this.merchant4 = merchant4;
    }

    public String getMerchant3() {
        return merchant3;
    }

    public void setMerchant3(String merchant3) {
        this.merchant3 = merchant3;
    }

    public String getMerchant2() {
        return merchant2;
    }

    public void setMerchant2(String merchant2) {
        this.merchant2 = merchant2;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getInstitutionname() {
        return institutionname;
    }

    public void setInstitutionname(String institutionname) {
        this.institutionname = institutionname;
    }

    public String getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(String institutionid) {
        this.institutionid = institutionid;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Date getDatestart() {
        return datestart;
    }

    public void setDatestart(Date datestart) {
        this.datestart = datestart;
    }

    public Date getDateend() {
        return dateend;
    }

    public void setDateend(Date dateend) {
        this.dateend = dateend;
    }

    public String getCustaccno() {
        return custaccno;
    }

    public void setCustaccno(String custaccno) {
        this.custaccno = custaccno;
    }

    public String getCorpabbr() {
        return corpabbr;
    }

    public void setCorpabbr(String corpabbr) {
        this.corpabbr = corpabbr;
    }

    public BigDecimal getCimbRegno() {
        return cimbRegno;
    }

    public void setCimbRegno(BigDecimal cimbRegno) {
        this.cimbRegno = cimbRegno;
    }

    public String getCimbName() {
        return cimbName;
    }

    public void setCimbName(String cimbName) {
        this.cimbName = cimbName;
    }

    public BigDecimal getCimb() {
        return cimb;
    }

    public void setCimb(BigDecimal cimb) {
        this.cimb = cimb;
    }

    public Integer getBillercode() {
        return billercode;
    }

    public void setBillercode(Integer billercode) {
        this.billercode = billercode;
    }

    public String getBankid4() {
        return bankid4;
    }

    public void setBankid4(String bankid4) {
        this.bankid4 = bankid4;
    }

    public String getBankid3() {
        return bankid3;
    }

    public void setBankid3(String bankid3) {
        this.bankid3 = bankid3;
    }

    public String getBankid2() {
        return bankid2;
    }

    public void setBankid2(String bankid2) {
        this.bankid2 = bankid2;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhaseno() {
        return phaseno;
    }

    public void setPhaseno(String phaseno) {
        this.phaseno = phaseno;
    }

}