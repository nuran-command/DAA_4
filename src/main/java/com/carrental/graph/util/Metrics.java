package com.carrental.graph.util;

/**
 * Common interface for collecting algorithm performance metrics.
 */
public interface Metrics {
    void start();
    void stop();
    long getTime();
    void increment(String key);
    long getCount(String key);
    void reset();
}