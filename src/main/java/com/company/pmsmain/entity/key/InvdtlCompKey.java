package com.company.pmsmain.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class InvdtlCompKey {
    @Column(name = "invno", nullable = false, unique = true)
    private Integer invno;

    @Column(name = "linenum", nullable = false, unique = true)
    private Short linenum;

    public Short getLinenum() {
        return linenum;
    }

    public void setLinenum(Short linenum) {
        this.linenum = linenum;
    }

    public Integer getInvno() {
        return invno;
    }

    public void setInvno(Integer invno) {
        this.invno = invno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(invno, linenum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvdtlCompKey entity = (InvdtlCompKey) o;
        return Objects.equals(this.invno, entity.invno) &&
                Objects.equals(this.linenum, entity.linenum);
    }

}