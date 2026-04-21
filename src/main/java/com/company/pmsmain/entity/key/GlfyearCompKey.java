package com.company.pmsmain.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@JmixEntity
@Embeddable
public class GlfyearCompKey {

    @Column(name = "compno", nullable = false, length = 2)
    private String compno;

    @Column(name = "fiscalyear", nullable = false, length = 10)
    private String fiscalyear;

    public String getCompno() {
        return compno;
    }

    public void setCompno(String compno) {
        this.compno = compno;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlfyearCompKey that = (GlfyearCompKey) o;
        return Objects.equals(compno, that.compno) &&
                Objects.equals(fiscalyear, that.fiscalyear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compno, fiscalyear);
    }
}
