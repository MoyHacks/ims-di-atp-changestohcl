package com.coppel.utils;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utilities {

    private Utilities() {}
    // METODO QUE VALIDA SI UN STRING ES NULO
    public static String unescapeCharacters(String str) {
        if (!isNullStr(str)) {
            str = unescapeJson(str);
            str = unescapeJava(str);
            str = unescapeXml(str);
            str = str.replaceAll("[\\n\\t ]", " ");
        }
        return str;
    }

  public static String unescapeJson(String input) {
        return input.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\/", "/")
                .replace("\\b", "\b")
                .replace("\\f", "\f")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("{\"","{");
    }

    private static String unescapeJava(String input) {
        return input.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\b", "\b")
                .replace("\\f", "\f")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }

    private static String unescapeXml(String input) {
        return input.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&apos;", "'");
    }

    private static boolean isNullStr(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().isEmpty();
    }
    public static String removeBackslashesFromJson(String jsonString) {
        return jsonString.replace("\\", "")
                .replace("\"key\":\"{", "\"key\": {")
                .replace("\"value\":\"{", "\"value\": {")
                .replace("}\",","},")
                .replace("}\"}]}","}}]}");
    }

}
