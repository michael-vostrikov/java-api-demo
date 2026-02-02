package com.app.userapi.helper;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;

public class SimpleHttpBuilder {

    public static String buildUrlQuery(Map<String, String> map) {
        if (map == null) {
          return "";
        }

        var list = new LinkedList<String>();
        for (var entry: map.entrySet()) {
            list.add(entry.getKey() + "=" + entry.getValue());
        }

        return String.join("&", list);
    }

    public static String buildJson(Map<String, String> map) {
        if (map == null) {
          return "";
        }

        var list = new LinkedList<String>();
        for (var entry: map.entrySet()) {
            list.add("  \"" + entry.getKey() + "\": \"" + entry.getValue() + "\"");
        }

        return "{\n" + String.join(",\n", list) + "\n}";
    }

}
