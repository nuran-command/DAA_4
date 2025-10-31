package com.carrental.graph.dagsp;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;
import com.carrental.graph.topo.TopologicalSorter;

import java.util.*;

public class DAGShortestPath {

    private final Metrics metrics;

    public DAGShortestPath() {
        this.metrics = new TimerMetrics();
    }

    public DAGShortestPath(Metrics metrics) {
        this.metrics = metrics;
    }

    public PathResult shortestPaths(Graph graph, int source, Map<String, Integer> weights) {
        metrics.start();
        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> order = sorter.sort(graph);

        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();

        for (int v : graph.getVertices()) dist.put(v, Integer.MAX_VALUE);
        dist.put(source, 0);

        for (int u : order) {
            if (dist.get(u) == Integer.MAX_VALUE) continue;
            for (int v : graph.getAdj(u)) {
                metrics.increment("Relaxations");
                int w = weights.getOrDefault(u + "-" + v, 1);
                if (dist.get(v) > dist.get(u) + w) {
                    dist.put(v, dist.get(u) + w);
                    parent.put(v, u);
                }
            }
        }

        metrics.stop();
        return new PathResult(dist, parent);
    }
    public Metrics getMetrics() {
        return this.metrics;
    }

    public void printMetrics() {
        System.out.println(metrics);
    }
}