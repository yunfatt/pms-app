package com.company.pmsmain.entity;

public enum PeriodEnum {

    PERIOD_1("1", "Period 1 - January"),
    PERIOD_2("2", "Period 2 - February"),
    PERIOD_3("3", "Period 3 - March"),
    PERIOD_4("4", "Period 4 - April"),
    PERIOD_5("5", "Period 5 - May"),
    PERIOD_6("6", "Period 6 - June"),
    PERIOD_7("7", "Period 7 - July"),
    PERIOD_8("8", "Period 8 - August"),
    PERIOD_9("9", "Period 9 - September"),
    PERIOD_10("10", "Period 10 - October"),
    PERIOD_11("11", "Period 11 - November"),
    PERIOD_12("12", "Period 12 - December");

    private final String id;
    private final String label;

    PeriodEnum(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static PeriodEnum fromValue(Short value) {
        if (value == null) return null;
        for (PeriodEnum p : values()) {
            if (p.id.equals(String.valueOf(value))) return p;
        }
        return null;
    }

    public Short toShort() {
        return Short.parseShort(id);
    }

    @Override
    public String toString() {
        return label;
    }
}