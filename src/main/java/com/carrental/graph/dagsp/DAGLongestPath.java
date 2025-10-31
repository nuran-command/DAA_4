package com.carrental.graph.dagsp;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;
import com.carrental.graph.topo.TopologicalSorter;

import java.util.*;

public class DAGLongestPath {

    private final Metrics metrics;

    public DAGLongestPath() {
        this.metrics = new TimerMetrics();
    }

    public DAGLongestPath(Metrics metrics) {
        this.metrics = metrics;
    }

    public PathResult longestPaths(Graph graph, int source, Map<String, Integer> weights) {
        metrics.start(); // start timing
        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> order = sorter.sort(graph);

        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();

        for (int v : graph.getVertices()) dist.put(v, Integer.MIN_VALUE);
        dist.put(source, 0);

        for (int u : order) {
            if (dist.get(u) == Integer.MIN_VALUE) continue;
            for (int v : graph.getAdj(u)) {
                metrics.increment("Relaxations"); // count edge relaxations
                int w = weights.getOrDefault(u + "-" + v, 1);
                if (dist.get(v) < dist.get(u) + w) {
                    dist.put(v, dist.get(u) + w);
                    parent.put(v, u);
                }
            }
        }

        metrics.stop(); // stop timing
        return new PathResult(dist, parent);
    }
    public Metrics getMetrics() {
        return this.metrics;
    }

    public void printMetrics() {
        System.out.println(metrics);
    }
}