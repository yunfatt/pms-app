package com.company.pmsmain.dto;

import java.time.LocalDate;

/**
 * DTO carrying parameters from Tab 1 (Parameters) to Tab 2 (Charge Lines)
 * and ultimately to GenInvoiceService.
 */
public class GenInvoiceParams {

    private String    propertyNo1;
    private String    propertyNo2;
    private LocalDate invoiceDate;
    private Integer   chargesMonth;
    private LocalDate generatePeriodFrom;
    private LocalDate generatePeriodTo;
    private Integer   taxInvType;        // 2=Inclusive, 3=Exclusive, 0=hidden
    private boolean   checkDuplicate;
    private Long      lastInvNo;

    public GenInvoiceParams() {}

    public String    getPropertyNo1()        { return propertyNo1; }
    public void      setPropertyNo1(String v){ this.propertyNo1 = v; }

    public String    getPropertyNo2()        { return propertyNo2; }
    public void      setPropertyNo2(String v){ this.propertyNo2 = v; }

    public LocalDate getInvoiceDate()           { return invoiceDate; }
    public void      setInvoiceDate(LocalDate v) { this.invoiceDate = v; }

    public Integer   getChargesMonth()          { return chargesMonth; }
    public void      setChargesMonth(Integer v)  { this.chargesMonth = v; }

    public LocalDate getGeneratePeriodFrom()           { return generatePeriodFrom; }
    public void      setGeneratePeriodFrom(LocalDate v) { this.generatePeriodFrom = v; }

    public LocalDate getGeneratePeriodTo()           { return generatePeriodTo; }
    public void      setGeneratePeriodTo(LocalDate v) { this.generatePeriodTo = v; }

    public Integer   getTaxInvType()          { return taxInvType; }
    public void      setTaxInvType(Integer v)  { this.taxInvType = v; }

    public boolean   isCheckDuplicate()           { return checkDuplicate; }
    public void      setCheckDuplicate(boolean v)  { this.checkDuplicate = v; }

    public Long      getLastInvNo()        { return lastInvNo; }
    public void      setLastInvNo(Long v)  { this.lastInvNo = v; }
}
