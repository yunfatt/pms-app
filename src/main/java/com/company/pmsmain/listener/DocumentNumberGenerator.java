package com.company.pmsmain.listener;

import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentNumberGenerator {

    @Autowired
    private DataManager dataManager;

    /**
     * Gets the next document number for any entity with a numeric number field.
     * e.g. getNextDocNo("Invoice", "invNo")
     *      getNextDocNo("DebitNote", "debitNo")
     *      getNextDocNo("CreditNote", "creditNo")
     */
    public Integer getNextDocNo(String entityName, String numberField) {
        Integer maxNo = dataManager.loadValue(
                String.format("select max(e.%s) from %s e", numberField, entityName),
                Integer.class
        ).optional().orElse(null);

        return maxNo == null ? 1 : maxNo + 1;
    }

    /**
     * Checks if a document number already exists for any entity.
     * e.g. existsDocNo("Invoice", "invNo", 5, currentId)
     */
    public boolean existsDocNo(String entityName, String numberField, Integer docNo, Object currentId) {
        if (docNo == null) return false;

        Long count = dataManager.loadValue(
                        String.format(
                                "select count(e) from %s e where e.%s = :docNo and (:id is null or e.id <> :id)",
                                entityName, numberField
                        ),
                        Long.class
                )
                .parameter("docNo", docNo)
                .parameter("id", currentId)
                .one();

        return count != null && count > 0;
    }
}