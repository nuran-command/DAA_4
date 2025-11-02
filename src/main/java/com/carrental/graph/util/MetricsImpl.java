package com.carrental.graph.util;

import org.json.JSONObject;
import java.util.*;

public class MetricsImpl {
    private final Map<String, Long> times = new LinkedHashMap<>();

    public void add(String name, long ns) {
        times.put(name, ns);
    }

    public void print() {
        System.out.println("\n=== PERFORMANCE SUMMARY ===");
        for (var e : times.entrySet())
            System.out.println(e.getKey() + ": " + e.getValue() + " ns");
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        for (var e : times.entrySet())
            obj.put(e.getKey(), e.getValue());
        return obj;
    }
}