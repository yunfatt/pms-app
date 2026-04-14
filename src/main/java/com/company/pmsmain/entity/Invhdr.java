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
@Table(name = "invhdr")
@Entity
public class Invhdr {
    @JmixGeneratedValue
    @Column(name = "invno", nullable = false)
    @Id
    private Integer invno;

    @Column(name = "addrtype", length = 3)
    private String addrtype;

    @Column(name = "amount", precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "compno", length = 2)
    private String compno;

    @Column(name = "custno")
    private Integer custno;

    @Column(name = "custtype")
    private Character custtype;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateadded")
    private Date dateadded;

    @Temporal(TemporalType.DATE)
    @Column(name = "datechanged")
    private Date datechanged;

    @Temporal(TemporalType.DATE)
    @Column(name = "datedeleted")
    private Date datedeleted;

    @Column(name = "deleted")
    private Short deleted;

    @Temporal(TemporalType.DATE)
    @Column(name = "einvdatecancelled")
    private Date einvdatecancelled;

    @Temporal(TemporalType.DATE)
    @Column(name = "einvdaterejected")
    private Date einvdaterejected;

    @Temporal(TemporalType.DATE)
    @Column(name = "einvdatesubmitted")
    private Date einvdatesubmitted;

    @Temporal(TemporalType.DATE)
    @Column(name = "einvdatevalidated")
    private Date einvdatevalidated;

    @Column(name = "einvlongid", length = 50)
    private String einvlongid;

    @Column(name = "einvsignature", length = 1000)
    private String einvsignature;

    @Column(name = "einvstatus")
    private Short einvstatus;

    @Column(name = "einvsubmissionuid", length = 26)
    private String einvsubmissionuid;

    @Temporal(TemporalType.TIME)
    @Column(name = "einvtimecancelled")
    private Date einvtimecancelled;

    @Temporal(TemporalType.TIME)
    @Column(name = "einvtimerejected")
    private Date einvtimerejected;

    @Column(name = "einvtimesubmitted")
    private Integer einvtimesubmitted;

    @Column(name = "einvtimevalidated")
    private Integer einvtimevalidated;

    @Column(name = "einvuin")
    private String einvuin;

    @Column(name = "einvvalidated")
    private Short einvvalidated;

    @Column(name = "filename", length = 30)
    private String filename;

    @Column(name = "netamt", precision = 14, scale = 2)
    private BigDecimal netamt;

    @Column(name = "phaseno", length = 6)
    private String phaseno;

    @Column(name = "posted")
    private Short posted;

    @Column(name = "printed")
    private Short printed;

    @Column(name = "propertyno", length = 12)
    private String propertyno;

    @Column(name = "refno", length = 30)
    private String refno;

    @Column(name = "refno2", length = 30)
    private String refno2;

    @Column(name = "remark1", length = 500)
    private String remark1;

    @Column(name = "remark2", length = 500)
    private String remark2;

    @Column(name = "remark3", length = 100)
    private String remark3;

    @Column(name = "taxableamt", precision = 10, scale = 2)
    private BigDecimal taxableamt;

    @Column(name = "taxinvtype")
    private Short taxinvtype;

    @Temporal(TemporalType.TIME)
    @Column(name = "timeadded")
    private Date timeadded;

    @Temporal(TemporalType.TIME)
    @Column(name = "timechanged")
    private Date timechanged;

    @Temporal(TemporalType.TIME)
    @Column(name = "timedeleted")
    private Date timedeleted;

    @Column(name = "totalgst", precision = 10, scale = 2)
    private BigDecimal totalgst;

    @Column(name = "totallines")
    private Integer totallines;

    @Temporal(TemporalType.DATE)
    @Column(name = "trxndate")
    private Date trxndate;

    @Column(name = "useradded", length = 20)
    private String useradded;

    @Column(name = "userchanged", length = 20)
    private String userchanged;

    @Column(name = "userdeleted", length = 20)
    private String userdeleted;

    @Column(name = "userid", length = 20)
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserdeleted() {
        return userdeleted;
    }

    public void setUserdeleted(String userdeleted) {
        this.userdeleted = userdeleted;
    }

    public String getUserchanged() {
        return userchanged;
    }

    public void setUserchanged(String userchanged) {
        this.userchanged = userchanged;
    }

    public String getUseradded() {
        return useradded;
    }

    public void setUseradded(String useradded) {
        this.useradded = useradded;
    }

    public Date getTrxndate() {
        return trxndate;
    }

    public void setTrxndate(Date trxndate) {
        this.trxndate = trxndate;
    }

    public Integer getTotallines() {
        return totallines;
    }

    public void setTotallines(Integer totallines) {
        this.totallines = totallines;
    }

    public BigDecimal getTotalgst() {
        return totalgst;
    }

    public void setTotalgst(BigDecimal totalgst) {
        this.totalgst = totalgst;
    }

    public Date getTimedeleted() {
        return timedeleted;
    }

    public void setTimedeleted(Date timedeleted) {
        this.timedeleted = timedeleted;
    }

    public Date getTimechanged() {
        return timechanged;
    }

    public void setTimechanged(Date timechanged) {
        this.timechanged = timechanged;
    }

    public Date getTimeadded() {
        return timeadded;
    }

    public void setTimeadded(Date timeadded) {
        this.timeadded = timeadded;
    }

    public Short getTaxinvtype() {
        return taxinvtype;
    }

    public void setTaxinvtype(Short taxinvtype) {
        this.taxinvtype = taxinvtype;
    }

    public BigDecimal getTaxableamt() {
        return taxableamt;
    }

    public void setTaxableamt(BigDecimal taxableamt) {
        this.taxableamt = taxableamt;
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

    public String getRefno2() {
        return refno2;
    }

    public void setRefno2(String refno2) {
        this.refno2 = refno2;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getPropertyno() {
        return propertyno;
    }

    public void setPropertyno(String propertyno) {
        this.propertyno = propertyno;
    }

    public Short getPrinted() {
        return printed;
    }

    public void setPrinted(Short printed) {
        this.printed = printed;
    }

    public Short getPosted() {
        return posted;
    }

    public void setPosted(Short posted) {
        this.posted = posted;
    }

    public String getPhaseno() {
        return phaseno;
    }

    public void setPhaseno(String phaseno) {
        this.phaseno = phaseno;
    }

    public BigDecimal getNetamt() {
        return netamt;
    }

    public void setNetamt(BigDecimal netamt) {
        this.netamt = netamt;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Short getEinvvalidated() {
        return einvvalidated;
    }

    public void setEinvvalidated(Short einvvalidated) {
        this.einvvalidated = einvvalidated;
    }

    public String getEinvuin() {
        return einvuin;
    }

    public void setEinvuin(String einvuin) {
        this.einvuin = einvuin;
    }

    public Integer getEinvtimevalidated() {
        return einvtimevalidated;
    }

    public void setEinvtimevalidated(Integer einvtimevalidated) {
        this.einvtimevalidated = einvtimevalidated;
    }

    public Integer getEinvtimesubmitted() {
        return einvtimesubmitted;
    }

    public void setEinvtimesubmitted(Integer einvtimesubmitted) {
        this.einvtimesubmitted = einvtimesubmitted;
    }

    public Date getEinvtimerejected() {
        return einvtimerejected;
    }

    public void setEinvtimerejected(Date einvtimerejected) {
        this.einvtimerejected = einvtimerejected;
    }

    public Date getEinvtimecancelled() {
        return einvtimecancelled;
    }

    public void setEinvtimecancelled(Date einvtimecancelled) {
        this.einvtimecancelled = einvtimecancelled;
    }

    public String getEinvsubmissionuid() {
        return einvsubmissionuid;
    }

    public void setEinvsubmissionuid(String einvsubmissionuid) {
        this.einvsubmissionuid = einvsubmissionuid;
    }

    public Short getEinvstatus() {
        return einvstatus;
    }

    public void setEinvstatus(Short einvstatus) {
        this.einvstatus = einvstatus;
    }

    public String getEinvsignature() {
        return einvsignature;
    }

    public void setEinvsignature(String einvsignature) {
        this.einvsignature = einvsignature;
    }

    public String getEinvlongid() {
        return einvlongid;
    }

    public void setEinvlongid(String einvlongid) {
        this.einvlongid = einvlongid;
    }

    public Date getEinvdatevalidated() {
        return einvdatevalidated;
    }

    public void setEinvdatevalidated(Date einvdatevalidated) {
        this.einvdatevalidated = einvdatevalidated;
    }

    public Date getEinvdatesubmitted() {
        return einvdatesubmitted;
    }

    public void setEinvdatesubmitted(Date einvdatesubmitted) {
        this.einvdatesubmitted = einvdatesubmitted;
    }

    public Date getEinvdaterejected() {
        return einvdaterejected;
    }

    public void setEinvdaterejected(Date einvdaterejected) {
        this.einvdaterejected = einvdaterejected;
    }

    public Date getEinvdatecancelled() {
        return einvdatecancelled;
    }

    public void setEinvdatecancelled(Date einvdatecancelled) {
        this.einvdatecancelled = einvdatecancelled;
    }

    public Short getDeleted() {
        return deleted;
    }

    public void setDeleted(Short deleted) {
        this.deleted = deleted;
    }

    public Date getDatedeleted() {
        return datedeleted;
    }

    public void setDatedeleted(Date datedeleted) {
        this.datedeleted = datedeleted;
    }

    public Date getDatechanged() {
        return datechanged;
    }

    public void setDatechanged(Date datechanged) {
        this.datechanged = datechanged;
    }

    public Date getDateadded() {
        return dateadded;
    }

    public void setDateadded(Date dateadded) {
        this.dateadded = dateadded;
    }

    public Character getCusttype() {
        return custtype;
    }

    public void setCusttype(Character custtype) {
        this.custtype = custtype;
    }

    public Integer getCustno() {
        return custno;
    }

    public void setCustno(Integer custno) {
        this.custno = custno;
    }

    public String getCompno() {
        return compno;
    }

    public void setCompno(String compno) {
        this.compno = compno;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddrtype() {
        return addrtype;
    }

    public void setAddrtype(String addrtype) {
        this.addrtype = addrtype;
    }

    public Integer getInvno() {
        return invno;
    }

    public void setInvno(Integer invno) {
        this.invno = invno;
    }

}