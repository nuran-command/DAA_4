package com.carrental.graph.util;

import java.util.*;

public class TimerMetrics implements Metrics {

    private final Map<String, Long> counters = new HashMap<>();
    private long startTime;
    private long elapsedTime;

    @Override
    public void start() {
        reset();
        startTime = System.nanoTime();
    }

    @Override
    public void stop() {
        elapsedTime = System.nanoTime() - startTime;
    }

    @Override
    public long getTime() {
        return elapsedTime;
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
        elapsedTime = 0;
    }

    @Override
    public String toString() {
        return "Time(ns)=" + elapsedTime + " Counters=" + counters;
    }
}