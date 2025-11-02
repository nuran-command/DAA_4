package com.carrental.graph.dagsp;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;
import com.carrental.graph.topo.TopologicalSorter;

import java.util.*;

/**
 * Computes the shortest paths in a Directed Acyclic Graph (DAG) from a given source vertex.
 * Uses topological sorting and dynamic programming for efficient distance computation.
 * Metrics are collected for performance analysis and number of relaxations.
 */
public class DAGShortestPath {

    private final Metrics metrics;

    /** Default constructor using TimerMetrics. */
    public DAGShortestPath() {
        this.metrics = new TimerMetrics();
    }

    /** Constructor using custom Metrics implementation. */
    public DAGShortestPath(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Computes shortest paths from the source vertex to all reachable vertices.
     * @param graph the DAG
     * @param source the starting vertex
     * @param weights edge weights as a map of "u-v" -> weight
     * @return PathResult containing distances, parents, best path, and metrics
     */
    public PathResult shortestPaths(Graph graph, int source, Map<String, Integer> weights) {
        metrics.start();

        // Step 1: Topologically sort the graph
        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> order = sorter.sort(graph);

        // Step 2: Initialize distance and parent maps
        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        for (int v : graph.getVertices()) dist.put(v, Integer.MAX_VALUE);
        dist.put(source, 0);

        // Step 3: Relax edges in topological order
        for (int u : order) {
            if (dist.get(u) == Integer.MAX_VALUE) continue; // unreachable
            for (int v : graph.getAdj(u)) {
                metrics.increment("relaxations");
                int w = weights.getOrDefault(u + "-" + v, 1);
                if (dist.get(v) > dist.get(u) + w) {
                    dist.put(v, dist.get(u) + w);
                    parent.put(v, u);
                }
            }
        }

        metrics.stop();
        return new PathResult(dist, parent, false);
    }

    public Metrics getMetrics() { return this.metrics; }

    public void printMetrics() { System.out.println(metrics); }
}