package com.company.pmsmain.entity;

import com.company.pmsmain.entity.converter.ShortBooleanConverter;
import com.company.pmsmain.entity.key.GlfyearCompKey;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.util.Date;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "tenant")
@Table(name = "glfyear")
@Entity
public class Glfyear {

    @EmbeddedId
    private GlfyearCompKey id;

    @InstanceName
    @Column(name = "fiscalyear", insertable = false, updatable = false, length = 10)
    private String fiscalyear;

    @Temporal(TemporalType.DATE)
    @Column(name = "initdate")
    private Date initdate;

    @Temporal(TemporalType.DATE)
    @Column(name = "finaldate")
    private Date finaldate;

    @Column(name = "accperiod")
    private Short accperiod;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl1")
    private Boolean periodctrl1;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl2")
    private Boolean periodctrl2;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl3")
    private Boolean periodctrl3;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl4")
    private Boolean periodctrl4;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl5")
    private Boolean periodctrl5;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl6")
    private Boolean periodctrl6;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl7")
    private Boolean periodctrl7;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl8")
    private Boolean periodctrl8;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl9")
    private Boolean periodctrl9;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl10")
    private Boolean periodctrl10;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl11")
    private Boolean periodctrl11;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl12")
    private Boolean periodctrl12;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl13")
    private Boolean periodctrl13;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl14")
    private Boolean periodctrl14;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl15")
    private Boolean periodctrl15;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl16")
    private Boolean periodctrl16;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl17")
    private Boolean periodctrl17;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "periodctrl18")
    private Boolean periodctrl18;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "audited")
    private Boolean audited;

    @Column(name = "auditedby", length = 50)
    private String auditedby;

    @Temporal(TemporalType.DATE)
    @Column(name = "auditeddate")
    private Date auditeddate;

    public GlfyearCompKey getId() { return id; }
    public void setId(GlfyearCompKey id) { this.id = id; }

    public String getFiscalyear() { return fiscalyear; }
    public void setFiscalyear(String fiscalyear) { this.fiscalyear = fiscalyear; }

    public Date getInitdate() { return initdate; }
    public void setInitdate(Date initdate) { this.initdate = initdate; }

    public Date getFinaldate() { return finaldate; }
    public void setFinaldate(Date finaldate) { this.finaldate = finaldate; }

    public Short getAccperiod() { return accperiod; }
    public void setAccperiod(Short accperiod) { this.accperiod = accperiod; }

    public Boolean getPeriodctrl1() { return periodctrl1; }
    public void setPeriodctrl1(Boolean periodctrl1) { this.periodctrl1 = periodctrl1; }

    public Boolean getPeriodctrl2() { return periodctrl2; }
    public void setPeriodctrl2(Boolean periodctrl2) { this.periodctrl2 = periodctrl2; }

    public Boolean getPeriodctrl3() { return periodctrl3; }
    public void setPeriodctrl3(Boolean periodctrl3) { this.periodctrl3 = periodctrl3; }

    public Boolean getPeriodctrl4() { return periodctrl4; }
    public void setPeriodctrl4(Boolean periodctrl4) { this.periodctrl4 = periodctrl4; }

    public Boolean getPeriodctrl5() { return periodctrl5; }
    public void setPeriodctrl5(Boolean periodctrl5) { this.periodctrl5 = periodctrl5; }

    public Boolean getPeriodctrl6() { return periodctrl6; }
    public void setPeriodctrl6(Boolean periodctrl6) { this.periodctrl6 = periodctrl6; }

    public Boolean getPeriodctrl7() { return periodctrl7; }
    public void setPeriodctrl7(Boolean periodctrl7) { this.periodctrl7 = periodctrl7; }

    public Boolean getPeriodctrl8() { return periodctrl8; }
    public void setPeriodctrl8(Boolean periodctrl8) { this.periodctrl8 = periodctrl8; }

    public Boolean getPeriodctrl9() { return periodctrl9; }
    public void setPeriodctrl9(Boolean periodctrl9) { this.periodctrl9 = periodctrl9; }

    public Boolean getPeriodctrl10() { return periodctrl10; }
    public void setPeriodctrl10(Boolean periodctrl10) { this.periodctrl10 = periodctrl10; }

    public Boolean getPeriodctrl11() { return periodctrl11; }
    public void setPeriodctrl11(Boolean periodctrl11) { this.periodctrl11 = periodctrl11; }

    public Boolean getPeriodctrl12() { return periodctrl12; }
    public void setPeriodctrl12(Boolean periodctrl12) { this.periodctrl12 = periodctrl12; }

    public Boolean getPeriodctrl13() { return periodctrl13; }
    public void setPeriodctrl13(Boolean periodctrl13) { this.periodctrl13 = periodctrl13; }

    public Boolean getPeriodctrl14() { return periodctrl14; }
    public void setPeriodctrl14(Boolean periodctrl14) { this.periodctrl14 = periodctrl14; }

    public Boolean getPeriodctrl15() { return periodctrl15; }
    public void setPeriodctrl15(Boolean periodctrl15) { this.periodctrl15 = periodctrl15; }

    public Boolean getPeriodctrl16() { return periodctrl16; }
    public void setPeriodctrl16(Boolean periodctrl16) { this.periodctrl16 = periodctrl16; }

    public Boolean getPeriodctrl17() { return periodctrl17; }
    public void setPeriodctrl17(Boolean periodctrl17) { this.periodctrl17 = periodctrl17; }

    public Boolean getPeriodctrl18() { return periodctrl18; }
    public void setPeriodctrl18(Boolean periodctrl18) { this.periodctrl18 = periodctrl18; }

    public Boolean getAudited() { return audited; }
    public void setAudited(Boolean audited) { this.audited = audited; }

    public String getAuditedby() { return auditedby; }
    public void setAuditedby(String auditedby) { this.auditedby = auditedby; }

    public Date getAuditeddate() { return auditeddate; }
    public void setAuditeddate(Date auditeddate) { this.auditeddate = auditeddate; }
}
