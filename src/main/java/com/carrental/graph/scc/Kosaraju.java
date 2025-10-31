package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;

import java.util.*;

public class Kosaraju {

    private final Metrics metrics;

    public Kosaraju() {
        this.metrics = new TimerMetrics();
    }

    public Kosaraju(Metrics metrics) {
        this.metrics = metrics;
    }

    public List<Component> findSCCs(Graph g) {
        metrics.start(); // start timing
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> order = new ArrayDeque<>();

        // Step 1: normal DFS to get finish order
        for (int v : g.getVertices()) {
            if (!visited.contains(v)) {
                dfs1(g, v, visited, order);
            }
        }

        // Step 2: transpose graph
        Graph gt = g.getTranspose();

        // Step 3: DFS on transposed graph in reverse finish order
        visited.clear();
        List<Component> components = new ArrayList<>();
        while (!order.isEmpty()) {
            int v = order.pop();
            metrics.increment("Stack-pops");
            if (!visited.contains(v)) {
                List<Integer> compNodes = new ArrayList<>();
                dfs2(gt, v, visited, compNodes);
                components.add(new Component(components.size(), compNodes));
            }
        }
        metrics.stop(); // stop timing
        return components;
    }

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
    public Metrics getMetrics() {
        return this.metrics;
    }

    public void printMetrics() {
        System.out.println(metrics);
    }
}