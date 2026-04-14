package com.company.pmsmain.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "tenant")
@Table(name = "customer")
@Entity
public class Customer {
    @JmixGeneratedValue
    @Column(name = "custno", nullable = false)
    @Id
    private Integer custno;

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

    @Column(name = "addrtype", length = 3)
    private String addrtype;

    @Temporal(TemporalType.DATE)
    @Column(name = "agreedate")
    private Date agreedate;

    @Column(name = "agreeno", length = 10)
    private String agreeno;

    @Column(name = "autotransfer")
    private Short autotransfer;

    @Column(name = "bf")
    private Short bf;

    @Column(name = "billtounit")
    private Character billtounit;

    @Temporal(TemporalType.DATE)
    @Column(name = "cardexpiredate")
    private Date cardexpiredate;

    @Column(name = "cardno", length = 16)
    private String cardno;

    @Column(name = "children", precision = 6, scale = 0)
    private BigDecimal children;

    @Column(name = "city", length = 40)
    private String city;

    @Temporal(TemporalType.DATE)
    @Column(name = "confirmlett")
    private Date confirmlett;

    @Temporal(TemporalType.DATE)
    @Column(name = "consentdate")
    private Date consentdate;

    @Column(name = "country", length = 30)
    private String country;

    @Column(name = "countrycode", length = 3)
    private String countrycode;

    @Column(name = "custname", length = 80)
    private String custname;

    @Column(name = "custname2", length = 80)
    private String custname2;

    @Column(name = "custname3", length = 80)
    private String custname3;

    @Column(name = "custname4", length = 80)
    private String custname4;

    @Column(name = "custname5", length = 80)
    private String custname5;

    @Column(name = "custname6", length = 80)
    private String custname6;

    @Column(name = "customerbrn", length = 20)
    private String customerbrn;

    @Column(name = "customergstno", length = 20)
    private String customergstno;

    @Column(name = "custtype", unique = true)
    private Character custtype;

    @Temporal(TemporalType.DATE)
    @Column(name = "datestart")
    private Date datestart;

    @Column(name = "disablesms")
    private Short disablesms;

    @Temporal(TemporalType.DATE)
    @Column(name = "dob")
    private Date dob;

    @Temporal(TemporalType.DATE)
    @Column(name = "dob2")
    private Date dob2;

    @Temporal(TemporalType.DATE)
    @Column(name = "dob3")
    private Date dob3;

    @Temporal(TemporalType.DATE)
    @Column(name = "dob4")
    private Date dob4;

    @Temporal(TemporalType.DATE)
    @Column(name = "dob5")
    private Date dob5;

    @Temporal(TemporalType.DATE)
    @Column(name = "dob6")
    private Date dob6;

    @Column(name = "einvoicecustomertype")
    private Character einvoicecustomertype;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "email2", length = 50)
    private String email2;

    @Column(name = "email3", length = 50)
    private String email3;

    @Column(name = "etel", length = 25)
    private String etel;

    @Temporal(TemporalType.DATE)
    @Column(name = "expire")
    private Date expire;

    @Column(name = "fax", length = 25)
    private String fax;

    @Column(name = "fincode", length = 6)
    private String fincode;

    @Column(name = "fincontacts")
    private String fincontacts;

    @Temporal(TemporalType.DATE)
    @Column(name = "fineffdate")
    private Date fineffdate;

    @Column(name = "finref", length = 40)
    private String finref;

    @Column(name = "fscode", length = 6)
    private String fscode;

    @Column(name = "fsref", length = 40)
    private String fsref;

    @Temporal(TemporalType.DATE)
    @Column(name = "gstenddate")
    private Date gstenddate;

    @Temporal(TemporalType.DATE)
    @Column(name = "gstregistrationdate")
    private Date gstregistrationdate;

    @Column(name = "historyno")
    private Short historyno;

    @Column(name = "htel", length = 25)
    private String htel;

    @Column(name = "htel2", length = 25)
    private String htel2;

    @Column(name = "identificationid", length = 30)
    private String identificationid;

    @Column(name = "identificationtype")
    private Character identificationtype;

    @Column(name = "income", precision = 12, scale = 2)
    private BigDecimal income;

    @Column(name = "interest")
    private Short interest;

    @Column(name = "legalaction")
    private Short legalaction;

    @Column(name = "motno", length = 10)
    private String motno;

    @Temporal(TemporalType.DATE)
    @Column(name = "motpresent")
    private Date motpresent;

    @Temporal(TemporalType.DATE)
    @Column(name = "motreceive")
    private Date motreceive;

    @Column(name = "mstatus")
    private Character mstatus;

    @Column(name = "national2", length = 3)
    private String national2;

    @Column(name = "national3", length = 3)
    private String national3;

    @Column(name = "national4", length = 3)
    private String national4;

    @Column(name = "national5", length = 3)
    private String national5;

    @Column(name = "national6", length = 3)
    private String national6;

    @Column(name = "nationality", length = 3)
    private String nationality;

    @Column(name = "nric1", length = 18)
    private String nric1;

    @Column(name = "nric2", length = 18)
    private String nric2;

    @Column(name = "nric3", length = 18)
    private String nric3;

    @Column(name = "nric4", length = 18)
    private String nric4;

    @Column(name = "nric5", length = 18)
    private String nric5;

    @Column(name = "nric6", length = 18)
    private String nric6;

    @Column(name = "otel", length = 25)
    private String otel;

    @Column(name = "otel2", length = 25)
    private String otel2;

    @Column(name = "phaseno", unique = true, length = 6)
    private String phaseno;

    @Column(name = "pob", length = 40)
    private String pob;

    @Column(name = "postcode", length = 6)
    private String postcode;

    @Column(name = "profession", length = 20)
    private String profession;

    @Column(name = "propertyno", unique = true, length = 12)
    private String propertyno;

    @Column(name = "race", length = 2)
    private String race;

    @Column(name = "registeredproprietor")
    private Short registeredproprietor;

    @Column(name = "religion", length = 10)
    private String religion;

    @Column(name = "remarks", length = 50)
    private String remarks;

    @Column(name = "remarks2", length = 50)
    private String remarks2;

    @Column(name = "sex")
    private Character sex;

    @Column(name = "solcode", length = 6)
    private String solcode;

    @Column(name = "solref", length = 40)
    private String solref;

    @Column(name = "spouseinc", precision = 12, scale = 2)
    private BigDecimal spouseinc;

    @Column(name = "spousejob", length = 50)
    private String spousejob;

    @Column(name = "spousename", length = 50)
    private String spousename;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "statecode", length = 2)
    private String statecode;

    @Column(name = "statement")
    private Short statement;

    @Column(name = "strata", length = 40)
    private String strata;

    @Temporal(TemporalType.DATE)
    @Column(name = "stratadate")
    private Date stratadate;

    @Column(name = "tel", length = 25)
    private String tel;

    @Column(name = "term", precision = 4, scale = 0)
    private BigDecimal term;

    @Column(name = "tin", length = 40)
    private String tin;

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getStratadate() {
        return stratadate;
    }

    public void setStratadate(Date stratadate) {
        this.stratadate = stratadate;
    }

    public String getStrata() {
        return strata;
    }

    public void setStrata(String strata) {
        this.strata = strata;
    }

    public Short getStatement() {
        return statement;
    }

    public void setStatement(Short statement) {
        this.statement = statement;
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

    public String getSpousename() {
        return spousename;
    }

    public void setSpousename(String spousename) {
        this.spousename = spousename;
    }

    public String getSpousejob() {
        return spousejob;
    }

    public void setSpousejob(String spousejob) {
        this.spousejob = spousejob;
    }

    public BigDecimal getSpouseinc() {
        return spouseinc;
    }

    public void setSpouseinc(BigDecimal spouseinc) {
        this.spouseinc = spouseinc;
    }

    public String getSolref() {
        return solref;
    }

    public void setSolref(String solref) {
        this.solref = solref;
    }

    public String getSolcode() {
        return solcode;
    }

    public void setSolcode(String solcode) {
        this.solcode = solcode;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Short getRegisteredproprietor() {
        return registeredproprietor;
    }

    public void setRegisteredproprietor(Short registeredproprietor) {
        this.registeredproprietor = registeredproprietor;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getPropertyno() {
        return propertyno;
    }

    public void setPropertyno(String propertyno) {
        this.propertyno = propertyno;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPob() {
        return pob;
    }

    public void setPob(String pob) {
        this.pob = pob;
    }

    public String getPhaseno() {
        return phaseno;
    }

    public void setPhaseno(String phaseno) {
        this.phaseno = phaseno;
    }

    public String getOtel2() {
        return otel2;
    }

    public void setOtel2(String otel2) {
        this.otel2 = otel2;
    }

    public String getOtel() {
        return otel;
    }

    public void setOtel(String otel) {
        this.otel = otel;
    }

    public String getNric6() {
        return nric6;
    }

    public void setNric6(String nric6) {
        this.nric6 = nric6;
    }

    public String getNric5() {
        return nric5;
    }

    public void setNric5(String nric5) {
        this.nric5 = nric5;
    }

    public String getNric4() {
        return nric4;
    }

    public void setNric4(String nric4) {
        this.nric4 = nric4;
    }

    public String getNric3() {
        return nric3;
    }

    public void setNric3(String nric3) {
        this.nric3 = nric3;
    }

    public String getNric2() {
        return nric2;
    }

    public void setNric2(String nric2) {
        this.nric2 = nric2;
    }

    public String getNric1() {
        return nric1;
    }

    public void setNric1(String nric1) {
        this.nric1 = nric1;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNational6() {
        return national6;
    }

    public void setNational6(String national6) {
        this.national6 = national6;
    }

    public String getNational5() {
        return national5;
    }

    public void setNational5(String national5) {
        this.national5 = national5;
    }

    public String getNational4() {
        return national4;
    }

    public void setNational4(String national4) {
        this.national4 = national4;
    }

    public String getNational3() {
        return national3;
    }

    public void setNational3(String national3) {
        this.national3 = national3;
    }

    public String getNational2() {
        return national2;
    }

    public void setNational2(String national2) {
        this.national2 = national2;
    }

    public Character getMstatus() {
        return mstatus;
    }

    public void setMstatus(Character mstatus) {
        this.mstatus = mstatus;
    }

    public Date getMotreceive() {
        return motreceive;
    }

    public void setMotreceive(Date motreceive) {
        this.motreceive = motreceive;
    }

    public Date getMotpresent() {
        return motpresent;
    }

    public void setMotpresent(Date motpresent) {
        this.motpresent = motpresent;
    }

    public String getMotno() {
        return motno;
    }

    public void setMotno(String motno) {
        this.motno = motno;
    }

    public Short getLegalaction() {
        return legalaction;
    }

    public void setLegalaction(Short legalaction) {
        this.legalaction = legalaction;
    }

    public Short getInterest() {
        return interest;
    }

    public void setInterest(Short interest) {
        this.interest = interest;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
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

    public String getHtel2() {
        return htel2;
    }

    public void setHtel2(String htel2) {
        this.htel2 = htel2;
    }

    public String getHtel() {
        return htel;
    }

    public void setHtel(String htel) {
        this.htel = htel;
    }

    public Short getHistoryno() {
        return historyno;
    }

    public void setHistoryno(Short historyno) {
        this.historyno = historyno;
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

    public String getFsref() {
        return fsref;
    }

    public void setFsref(String fsref) {
        this.fsref = fsref;
    }

    public String getFscode() {
        return fscode;
    }

    public void setFscode(String fscode) {
        this.fscode = fscode;
    }

    public String getFinref() {
        return finref;
    }

    public void setFinref(String finref) {
        this.finref = finref;
    }

    public Date getFineffdate() {
        return fineffdate;
    }

    public void setFineffdate(Date fineffdate) {
        this.fineffdate = fineffdate;
    }

    public String getFincontacts() {
        return fincontacts;
    }

    public void setFincontacts(String fincontacts) {
        this.fincontacts = fincontacts;
    }

    public String getFincode() {
        return fincode;
    }

    public void setFincode(String fincode) {
        this.fincode = fincode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public String getEtel() {
        return etel;
    }

    public void setEtel(String etel) {
        this.etel = etel;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Character getEinvoicecustomertype() {
        return einvoicecustomertype;
    }

    public void setEinvoicecustomertype(Character einvoicecustomertype) {
        this.einvoicecustomertype = einvoicecustomertype;
    }

    public Date getDob6() {
        return dob6;
    }

    public void setDob6(Date dob6) {
        this.dob6 = dob6;
    }

    public Date getDob5() {
        return dob5;
    }

    public void setDob5(Date dob5) {
        this.dob5 = dob5;
    }

    public Date getDob4() {
        return dob4;
    }

    public void setDob4(Date dob4) {
        this.dob4 = dob4;
    }

    public Date getDob3() {
        return dob3;
    }

    public void setDob3(Date dob3) {
        this.dob3 = dob3;
    }

    public Date getDob2() {
        return dob2;
    }

    public void setDob2(Date dob2) {
        this.dob2 = dob2;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Short getDisablesms() {
        return disablesms;
    }

    public void setDisablesms(Short disablesms) {
        this.disablesms = disablesms;
    }

    public Date getDatestart() {
        return datestart;
    }

    public void setDatestart(Date datestart) {
        this.datestart = datestart;
    }

    public Character getCusttype() {
        return custtype;
    }

    public void setCusttype(Character custtype) {
        this.custtype = custtype;
    }

    public String getCustomergstno() {
        return customergstno;
    }

    public void setCustomergstno(String customergstno) {
        this.customergstno = customergstno;
    }

    public String getCustomerbrn() {
        return customerbrn;
    }

    public void setCustomerbrn(String customerbrn) {
        this.customerbrn = customerbrn;
    }

    public String getCustname6() {
        return custname6;
    }

    public void setCustname6(String custname6) {
        this.custname6 = custname6;
    }

    public String getCustname5() {
        return custname5;
    }

    public void setCustname5(String custname5) {
        this.custname5 = custname5;
    }

    public String getCustname4() {
        return custname4;
    }

    public void setCustname4(String custname4) {
        this.custname4 = custname4;
    }

    public String getCustname3() {
        return custname3;
    }

    public void setCustname3(String custname3) {
        this.custname3 = custname3;
    }

    public String getCustname2() {
        return custname2;
    }

    public void setCustname2(String custname2) {
        this.custname2 = custname2;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
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

    public Date getConsentdate() {
        return consentdate;
    }

    public void setConsentdate(Date consentdate) {
        this.consentdate = consentdate;
    }

    public Date getConfirmlett() {
        return confirmlett;
    }

    public void setConfirmlett(Date confirmlett) {
        this.confirmlett = confirmlett;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BigDecimal getChildren() {
        return children;
    }

    public void setChildren(BigDecimal children) {
        this.children = children;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public Date getCardexpiredate() {
        return cardexpiredate;
    }

    public void setCardexpiredate(Date cardexpiredate) {
        this.cardexpiredate = cardexpiredate;
    }

    public Character getBilltounit() {
        return billtounit;
    }

    public void setBilltounit(Character billtounit) {
        this.billtounit = billtounit;
    }

    public Short getBf() {
        return bf;
    }

    public void setBf(Short bf) {
        this.bf = bf;
    }

    public Short getAutotransfer() {
        return autotransfer;
    }

    public void setAutotransfer(Short autotransfer) {
        this.autotransfer = autotransfer;
    }

    public String getAgreeno() {
        return agreeno;
    }

    public void setAgreeno(String agreeno) {
        this.agreeno = agreeno;
    }

    public Date getAgreedate() {
        return agreedate;
    }

    public void setAgreedate(Date agreedate) {
        this.agreedate = agreedate;
    }

    public String getAddrtype() {
        return addrtype;
    }

    public void setAddrtype(String addrtype) {
        this.addrtype = addrtype;
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

    public Integer getCustno() {
        return custno;
    }

    public void setCustno(Integer custno) {
        this.custno = custno;
    }

}