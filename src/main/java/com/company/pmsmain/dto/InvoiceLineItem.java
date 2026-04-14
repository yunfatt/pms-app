package com.company.pmsmain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * One charge line row in the Generate Invoice grid (TmpDtl / ChildList).
 * Fields: TmD:ChargeNo, TmD:DESC, TmD:Date1, TmD:Date2, TmD:DueDate,
 *         TmD:QTY/PRICE, TmD:QTY2/PRICE2, TmD:QTY3/PRICE3
 */
public class InvoiceLineItem {

    private String     chargeNo;
    private String     description;
    private LocalDate  dateFrom;
    private LocalDate  dateTo;
    private LocalDate  dueDate;
    private BigDecimal qty;
    private BigDecimal price;
    private BigDecimal qty2;
    private BigDecimal price2;
    private BigDecimal qty3;
    private BigDecimal price3;

    public InvoiceLineItem() {}

    public String    getChargeNo()              { return chargeNo; }
    public void      setChargeNo(String v)       { this.chargeNo = v; }

    public String    getDescription()           { return description; }
    public void      setDescription(String v)    { this.description = v; }

    public LocalDate getDateFrom()              { return dateFrom; }
    public void      setDateFrom(LocalDate v)    { this.dateFrom = v; }

    public LocalDate getDateTo()                { return dateTo; }
    public void      setDateTo(LocalDate v)      { this.dateTo = v; }

    public LocalDate getDueDate()               { return dueDate; }
    public void      setDueDate(LocalDate v)     { this.dueDate = v; }

    public BigDecimal getQty()                  { return qty; }
    public void       setQty(BigDecimal v)       { this.qty = v; }

    public BigDecimal getPrice()                { return price; }
    public void       setPrice(BigDecimal v)     { this.price = v; }

    public BigDecimal getQty2()                 { return qty2; }
    public void       setQty2(BigDecimal v)      { this.qty2 = v; }

    public BigDecimal getPrice2()               { return price2; }
    public void       setPrice2(BigDecimal v)    { this.price2 = v; }

    public BigDecimal getQty3()                 { return qty3; }
    public void       setQty3(BigDecimal v)      { this.qty3 = v; }

    public BigDecimal getPrice3()               { return price3; }
    public void       setPrice3(BigDecimal v)    { this.price3 = v; }
}
