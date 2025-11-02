package com.carrental.graph.util;

import org.json.JSONObject;
import java.util.*;

/**
 * Simple implementation to store multiple named metrics and output as JSON.
 * Used for storing results of algorithm runs.
 */
public class MetricsImpl {

    private final Map<String, Long> times = new LinkedHashMap<>();

    /**
     * Adds a named metric value.
     *
     * @param name metric name
     * @param ns metric value in nanoseconds
     */
    public void add(String name, long ns) {
        times.put(name, ns);
    }

    /**
     * Prints all stored metrics to standard output.
     */
    public void print() {
        System.out.println("\n=== PERFORMANCE SUMMARY ===");
        for (var e : times.entrySet())
            System.out.println(e.getKey() + ": " + e.getValue() + " ns");
    }

    /**
     * Converts metrics to a JSON object.
     *
     * @return JSONObject containing all metrics
     */
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        for (var e : times.entrySet())
            obj.put(e.getKey(), e.getValue());
        return obj;
    }
}
