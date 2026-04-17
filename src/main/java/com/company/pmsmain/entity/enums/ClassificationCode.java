
package com.company.pmsmain.entity.enums;

public enum ClassificationCode {
    CODE_001("001", "Breastfeeding equipment"),
    CODE_002("002", "Child care centres and kindergartens fees"),
    CODE_003("003", "Computer, smartphone or tablet"),
    CODE_004("004", "Consolidated e-Invoice"),
    CODE_005("005", "Construction materials (Fourth Schedule, CIDB Act 1994)"),
    CODE_006("006", "Disbursement"),
    CODE_007("007", "Donation"),
    CODE_008("008", "e-Commerce - e-Invoice to buyer / purchaser"),
    CODE_009("009", "e-Commerce - Self-billed e-Invoice to seller, logistics, etc."),
    CODE_010("010", "Education fees"),
    CODE_011("011", "Goods on consignment (Consignor)"),
    CODE_012("012", "Goods on consignment (Consignee)"),
    CODE_013("013", "Gym membership"),
    CODE_014("014", "Insurance - Education and medical benefits"),
    CODE_015("015", "Insurance - Takaful or life insurance"),
    CODE_016("016", "Interest and financing expenses"),
    CODE_017("017", "Internet subscription"),
    CODE_018("018", "Land and building"),
    CODE_019("019", "Medical examination for learning disabilities and early intervention/rehabilitation"),
    CODE_020("020", "Medical examination or vaccination expenses"),
    CODE_021("021", "Medical expenses for serious diseases"),
    CODE_022("022", "Others"),
    CODE_023("023", "Petroleum operations (Petroleum Income Tax Act 1967)"),
    CODE_024("024", "Private retirement scheme or deferred annuity scheme"),
    CODE_025("025", "Motor vehicle"),
    CODE_026("026", "Subscription of books / journals / magazines / newspapers / publications"),
    CODE_027("027", "Reimbursement"),
    CODE_028("028", "Rental of motor vehicle"),
    CODE_029("029", "EV charging facilities (installation, rental, sale/purchase or subscription)"),
    CODE_030("030", "Repair and maintenance"),
    CODE_031("031", "Research and development"),
    CODE_032("032", "Foreign income"),
    CODE_033("033", "Self-billed - Betting and gaming"),
    CODE_034("034", "Self-billed - Importation of goods"),
    CODE_035("035", "Self-billed - Importation of services"),
    CODE_036("036", "Self-billed - Others"),
    CODE_037("037", "Self-billed - Monetary payment to agents, dealers or distributors"),
    CODE_038("038", "Sports equipment, rental / entry fees for sports facilities and competitions"),
    CODE_039("039", "Supporting equipment for disabled person"),
    CODE_040("040", "Voluntary contribution to approved provident fund"),
    CODE_041("041", "Dental examination or treatment"),
    CODE_042("042", "Fertility treatment"),
    CODE_043("043", "Treatment and home care nursing, daycare centres and residential care centers"),
    CODE_044("044", "Vouchers, gift cards, loyalty points, etc."),
    CODE_045("045", "Self-billed - Non-monetary payment to agents, dealers or distributors");

    private final String code;
    private final String description;

    ClassificationCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }

    @Override
    public String toString() { return code + " – " + description; }
}