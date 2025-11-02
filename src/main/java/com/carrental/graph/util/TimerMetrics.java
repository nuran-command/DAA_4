package com.carrental.graph.util;

import java.util.*;

/**
 * Tracks algorithm performance time and operation counts.
 */
public class TimerMetrics implements Metrics {

    private long startTime;
    private long endTime;
    private boolean running;
    private final Map<String, Long> counters = new HashMap<>();

    @Override
    public void start() {
        running = true;
        startTime = System.nanoTime();
    }

    @Override
    public void stop() {
        if (running) {
            endTime = System.nanoTime();
            running = false;
        }
    }

    @Override
    public long getTime() {
        return endTime - startTime;
    }

    @Override
    public void increment(String key) {
        counters.put(key, counters.getOrDefault(key, 0L) + 1);
    }

    @Override
    public long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    @Override
    public void reset() {
        counters.clear();
        startTime = 0;
        endTime = 0;
        running = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Execution time: " + getTime() + " ns");
        if (!counters.isEmpty()) {
            sb.append("\nOperation counts: ");
            for (var e : counters.entrySet()) {
                sb.append(e.getKey()).append("=").append(e.getValue()).append(" ");
            }
        }
        return sb.toString();
    }
}