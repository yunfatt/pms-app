package com.company.pmsmain.util;

public class DocumentFormatUtils {

    private DocumentFormatUtils() {}

    public static String formatDocNo(Integer no) {
        if (no == null) return "";
        return String.format("%07d", no);
    }
}