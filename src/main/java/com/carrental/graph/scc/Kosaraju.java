package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;

import java.util.*;

/**
 * Implements Kosaraju's algorithm to find Strongly Connected Components (SCCs)
 * in a directed graph.
 *
 * Steps:
 * 1. DFS on original graph to compute finishing times (dfs1).
 * 2. Transpose the graph.
 * 3. DFS on transposed graph in reverse finishing order to collect SCCs (dfs2).
 *
 * Metrics are recorded for instrumentation (visits, edges, stack operations).
 */
public class Kosaraju {

    private final Metrics metrics;

    /** Default constructor uses TimerMetrics for instrumentation */
    public Kosaraju() {
        this.metrics = new TimerMetrics();
    }

    /** Constructor allows custom Metrics implementation */
    public Kosaraju(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Finds strongly connected components in the given graph.
     *
     * @param g Input directed graph
     * @return List of Components, each representing an SCC
     */
    public List<Component> findSCCs(Graph g) {
        metrics.start();

        Set<Integer> visited = new HashSet<>();
        Deque<Integer> finishOrder = new ArrayDeque<>();

        // Phase 1: DFS on original graph to compute finishing order
        for (int v : g.getVertices()) {
            if (!visited.contains(v)) {
                dfs1(g, v, visited, finishOrder);
            }
        }

        // Phase 2: transpose the graph
        Graph transposed = g.getTranspose();

        // Phase 3: DFS on transposed graph in reverse finishing order
        visited.clear();
        List<Component> components = new ArrayList<>();
        while (!finishOrder.isEmpty()) {
            int v = finishOrder.pop();
            metrics.increment("Stack-pops");
            if (!visited.contains(v)) {
                List<Integer> compNodes = new ArrayList<>();
                dfs2(transposed, v, visited, compNodes);

                // Optional: sort nodes for deterministic test outputs
                Collections.sort(compNodes);

                components.add(new Component(components.size(), compNodes));
            }
        }

        metrics.stop();
        return components;
    }

    /**
     * DFS phase 1: computes finishing order of nodes.
     * @param g Graph
     * @param u Current node
     * @param visited Visited set
     * @param order Stack to store finishing order
     */
    private void dfs1(Graph g, int u, Set<Integer> visited, Deque<Integer> order) {
        visited.add(u);
        metrics.increment("DFS1-visits");

        for (int v : g.getAdj(u)) {
            metrics.increment("DFS1-edges");
            if (!visited.contains(v)) {
                dfs1(g, v, visited, order);
            }
        }

        order.push(u);
        metrics.increment("Stack-pushes");
    }

    /**
     * DFS phase 2: collects nodes in SCC on transposed graph.
     * @param g Transposed graph
     * @param u Current node
     * @param visited Visited set
     * @param compNodes List collecting nodes in the current SCC
     */
    private void dfs2(Graph g, int u, Set<Integer> visited, List<Integer> compNodes) {
        visited.add(u);
        metrics.increment("DFS2-visits");

        compNodes.add(u);

        for (int v : g.getAdj(u)) {
            metrics.increment("DFS2-edges");
            if (!visited.contains(v)) {
                dfs2(g, v, visited, compNodes);
            }
        }
    }

    /** Returns metrics collected during algorithm execution */
    public Metrics getMetrics() {
        return this.metrics;
    }

    /** Prints collected metrics */
    public void printMetrics() {
        System.out.println(metrics);
    }
}