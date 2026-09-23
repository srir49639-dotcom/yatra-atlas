package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight JSON parser and builder for the Java HTTP REST API.
 * Eliminates external Maven/Gradle dependencies so the project compiles directly with standard JDK.
 */
public class SimpleJson {

    public static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (ch < ' ') {
                        String hex = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - hex.length(); k++) sb.append('0');
                        sb.append(hex);
                    } else {
                        sb.append(ch);
                    }
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * Parses simple top-level key-value JSON objects like {"query":"fort","from":"hyderabad","to":"goa"}
     */
    public static Map<String, String> parseSimpleObject(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null) return map;
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1).trim();
        }

        boolean inQuote = false;
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean parsingKey = true;
        boolean inArray = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inQuote = !inQuote;
                continue;
            }

            if (!inQuote && c == '[') {
                inArray = true;
                value.append(c);
                continue;
            }
            if (!inQuote && c == ']') {
                inArray = false;
                value.append(c);
                continue;
            }

            if (!inQuote && !inArray && c == ':') {
                parsingKey = false;
                continue;
            }

            if (!inQuote && !inArray && c == ',') {
                String k = key.toString().trim();
                String v = value.toString().trim();
                if (!k.isEmpty()) {
                    map.put(k, v);
                }
                key.setLength(0);
                value.setLength(0);
                parsingKey = true;
                continue;
            }

            if (parsingKey) {
                key.append(c);
            } else {
                value.append(c);
            }
        }

        String k = key.toString().trim();
        String v = value.toString().trim();
        if (!k.isEmpty()) {
            map.put(k, v);
        }

        return map;
    }
}
