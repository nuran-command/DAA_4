package com.carrental.graph.util;

import org.json.JSONObject;
import java.util.*;

/**
 * Implements a timer with operation counters to measure algorithm performance.
 * Supports start/stop timing and incrementing named counters.
 */
public class TimerMetrics implements Metrics {

    private long startTime;
    private long endTime;
    private boolean running;
    private final Map<String, Long> counters = new HashMap<>();

    /** Starts the timer. */
    @Override
    public void start() {
        running = true;
        startTime = System.nanoTime();
    }

    /** Stops the timer. */
    @Override
    public void stop() {
        if (running) {
            endTime = System.nanoTime();
            running = false;
        }
    }

    /**
     * Returns the elapsed time between start and stop.
     *
     * @return execution time in nanoseconds
     */
    @Override
    public long getTime() {
        return endTime - startTime;
    }

    /**
     * Increments the counter for a given operation.
     *
     * @param key name of the operation
     */
    @Override
    public void increment(String key) {
        counters.put(key, counters.getOrDefault(key, 0L) + 1);
    }

    /**
     * Returns the current count for a given operation.
     *
     * @param key name of the operation
     * @return count value
     */
    @Override
    public long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    /** Resets the timer and all counters. */
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