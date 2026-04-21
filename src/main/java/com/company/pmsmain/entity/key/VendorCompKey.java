package com.company.pmsmain.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class VendorCompKey {
    @Column(name = "compno", nullable = false, unique = true, length = 2)
    private String compno;

    @Column(name = "vdrno", nullable = false, unique = true, length = 8)
    private String vdrno;

    public String getVdrno() {
        return vdrno;
    }

    public void setVdrno(String vdrno) {
        this.vdrno = vdrno;
    }

    public String getCompno() {
        return compno;
    }

    public void setCompno(String compno) {
        this.compno = compno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vdrno, compno);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendorCompKey entity = (VendorCompKey) o;
        return Objects.equals(this.vdrno, entity.vdrno) &&
                Objects.equals(this.compno, entity.compno);
    }

}