
package com.carrental.graph.util;
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
}