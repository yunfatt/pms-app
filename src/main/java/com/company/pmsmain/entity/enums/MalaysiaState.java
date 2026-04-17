package com.company.pmsmain.entity.enums;

public enum MalaysiaState {

    STATE_01("01", "Johor"),
    STATE_02("02", "Kedah"),
    STATE_03("03", "Kelantan"),
    STATE_04("04", "Melaka"),
    STATE_05("05", "Negeri Sembilan"),
    STATE_06("06", "Pahang"),
    STATE_07("07", "Pulau Pinang"),
    STATE_08("08", "Perak"),
    STATE_09("09", "Perlis"),
    STATE_10("10", "Selangor"),
    STATE_11("11", "Terengganu"),
    STATE_12("12", "Sabah"),
    STATE_13("13", "Sarawak"),
    STATE_14("14", "Wilayah Persekutuan Kuala Lumpur"),
    STATE_15("15", "Wilayah Persekutuan Labuan"),
    STATE_16("16", "Wilayah Persekutuan Putrajaya"),
    STATE_17("17", "Not Applicable");

    private final String code;
    private final String state;

    MalaysiaState(String code, String state) {
        this.code  = code;
        this.state = state;
    }

    public String getCode()  { return code; }
    public String getState() { return state; }

    @Override
    public String toString() { return code + " – " + state; }
}