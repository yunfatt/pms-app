package com.company.pmsmain.entity;

import com.company.pmsmain.entity.converter.ShortBooleanConverter;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "tenant")
@Table(name = "glacc")
@Entity
public class Glacc {

    @Id
    @Column(name = "accno", nullable = false, length = 8)
    private String accno;

    @InstanceName
    @Column(name = "description", length = 40)
    private String description;

    @Column(name = "control")
    private Short control;

    @Column(name = "trxntype", length = 1)
    private String trxntype;

    @Column(name = "groupcode", length = 8)
    private String groupcode;

    @Column(name = "industrycode", length = 10)
    private String industrycode;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "active")
    private Boolean active;

    @Convert(converter = ShortBooleanConverter.class)
    @Column(name = "budgetcontrol")
    private Boolean budgetcontrol;

    @Column(name = "remark1", length = 30)
    private String remark1;

    @Column(name = "remark2", length = 30)
    private String remark2;

    @Column(name = "remark3", length = 30)
    private String remark3;

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getControl() {
        return control;
    }

    public void setControl(Short control) {
        this.control = control;
    }

    public String getTrxntype() {
        return trxntype;
    }

    public void setTrxntype(String trxntype) {
        this.trxntype = trxntype;
    }

    public String getGroupcode() {
        return groupcode;
    }

    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }

    public String getIndustrycode() {
        return industrycode;
    }

    public void setIndustrycode(String industrycode) {
        this.industrycode = industrycode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getBudgetcontrol() {
        return budgetcontrol;
    }

    public void setBudgetcontrol(Boolean budgetcontrol) {
        this.budgetcontrol = budgetcontrol;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }
}