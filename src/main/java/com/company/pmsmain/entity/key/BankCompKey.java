package com.company.pmsmain.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class BankCompKey {
    @Column(name = "bank_code", nullable = false, unique = true, length = 8)
    private String bankCode;

    @Column(name = "compno", nullable = false, unique = true, length = 2)
    private String compno;

    public String getCompno() {
        return compno;
    }

    public void setCompno(String compno) {
        this.compno = compno;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankCode, compno);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankCompKey entity = (BankCompKey) o;
        return Objects.equals(this.bankCode, entity.bankCode) &&
                Objects.equals(this.compno, entity.compno);
    }

}