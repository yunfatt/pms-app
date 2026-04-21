package com.company.pmsmain.entity;

import com.company.pmsmain.entity.key.VendorCompKey;
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
@Table(name = "vendor")
@Entity
public class Vendor {
    @EmbeddedId
    private VendorCompKey id;

    @Column(name = "accno", length = 8)
    private String accno;

    @Column(name = "active")
    private Short active;

    @Column(name = "addr1", length = 50)
    private String addr1;

    @Column(name = "addr2", length = 50)
    private String addr2;

    @Column(name = "addr3", length = 50)
    private String addr3;

    @Column(name = "addr4", length = 50)
    private String addr4;

    @Column(name = "area", length = 2)
    private String area;

    @Column(name = "bank_acctno", length = 20)
    private String bankAcctno;

    @Column(name = "bank_code", length = 15)
    private String bankCode;

    @Column(name = "city", length = 40)
    private String city;

    @Column(name = "contact", length = 50)
    private String contact;

    @Column(name = "country", length = 30)
    private String country;

    @Column(name = "countrycode", length = 3)
    private String countrycode;

    @Column(name = "creditlimit", precision = 8, scale = 2)
    private BigDecimal creditlimit;

    @Column(name = "email")
    private String email;

    @Column(name = "enableautopay")
    private Short enableautopay;

    @Column(name = "fax", length = 30)
    private String fax;

    @Column(name = "groupno", length = 6)
    private String groupno;

    @Temporal(TemporalType.DATE)
    @Column(name = "gstenddate")
    private Date gstenddate;

    @Temporal(TemporalType.DATE)
    @Column(name = "gstregistrationdate")
    private Date gstregistrationdate;

    @Column(name = "identificationid", length = 30)
    private String identificationid;

    @Column(name = "identificationtype")
    private Character identificationtype;

    @Column(name = "postcode", length = 6)
    private String postcode;

    @Column(name = "prjno", length = 8)
    private String prjno;

    @Column(name = "remark1", length = 50)
    private String remark1;

    @Column(name = "remark2", length = 50)
    private String remark2;

    @Column(name = "remark3", length = 50)
    private String remark3;

    @Temporal(TemporalType.DATE)
    @Column(name = "selfbilledapprovaldate")
    private Date selfbilledapprovaldate;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "statecode", length = 2)
    private String statecode;

    @Column(name = "supplierbrn", length = 50)
    private String supplierbrn;

    @Column(name = "suppliergst", length = 20)
    private String suppliergst;

    @Column(name = "tel", length = 30)
    private String tel;

    @Column(name = "tel2", length = 30)
    private String tel2;

    @Column(name = "term", precision = 8, scale = 2)
    private BigDecimal term;

    @Column(name = "tin", length = 40)
    private String tin;

    @Column(name = "totdue", precision = 8, scale = 2)
    private BigDecimal totdue;

    @InstanceName
    @Column(name = "vdrname", length = 60)
    private String vdrname;

    @Column(name = "vdrname2", length = 60)
    private String vdrname2;

    public String getVdrname2() {
        return vdrname2;
    }

    public void setVdrname2(String vdrname2) {
        this.vdrname2 = vdrname2;
    }

    public String getVdrname() {
        return vdrname;
    }

    public void setVdrname(String vdrname) {
        this.vdrname = vdrname;
    }

    public BigDecimal getTotdue() {
        return totdue;
    }

    public void setTotdue(BigDecimal totdue) {
        this.totdue = totdue;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public BigDecimal getTerm() {
        return term;
    }

    public void setTerm(BigDecimal term) {
        this.term = term;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSuppliergst() {
        return suppliergst;
    }

    public void setSuppliergst(String suppliergst) {
        this.suppliergst = suppliergst;
    }

    public String getSupplierbrn() {
        return supplierbrn;
    }

    public void setSupplierbrn(String supplierbrn) {
        this.supplierbrn = supplierbrn;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getSelfbilledapprovaldate() {
        return selfbilledapprovaldate;
    }

    public void setSelfbilledapprovaldate(Date selfbilledapprovaldate) {
        this.selfbilledapprovaldate = selfbilledapprovaldate;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getPrjno() {
        return prjno;
    }

    public void setPrjno(String prjno) {
        this.prjno = prjno;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Character getIdentificationtype() {
        return identificationtype;
    }

    public void setIdentificationtype(Character identificationtype) {
        this.identificationtype = identificationtype;
    }

    public String getIdentificationid() {
        return identificationid;
    }

    public void setIdentificationid(String identificationid) {
        this.identificationid = identificationid;
    }

    public Date getGstregistrationdate() {
        return gstregistrationdate;
    }

    public void setGstregistrationdate(Date gstregistrationdate) {
        this.gstregistrationdate = gstregistrationdate;
    }

    public Date getGstenddate() {
        return gstenddate;
    }

    public void setGstenddate(Date gstenddate) {
        this.gstenddate = gstenddate;
    }

    public String getGroupno() {
        return groupno;
    }

    public void setGroupno(String groupno) {
        this.groupno = groupno;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Short getEnableautopay() {
        return enableautopay;
    }

    public void setEnableautopay(Short enableautopay) {
        this.enableautopay = enableautopay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getCreditlimit() {
        return creditlimit;
    }

    public void setCreditlimit(BigDecimal creditlimit) {
        this.creditlimit = creditlimit;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAcctno() {
        return bankAcctno;
    }

    public void setBankAcctno(String bankAcctno) {
        this.bankAcctno = bankAcctno;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public Short getActive() {
        return active;
    }

    public void setActive(Short active) {
        this.active = active;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public VendorCompKey getId() {
        return id;
    }

    public void setId(VendorCompKey id) {
        this.id = id;
    }

}