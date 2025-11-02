package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;

import java.util.*;

public class TopologicalSorter {

    private final Metrics metrics;

    public TopologicalSorter() {
        this.metrics = new TimerMetrics();
    }

    public TopologicalSorter(Metrics metrics) {
        this.metrics = metrics;
    }

    public List<Integer> sort(Graph graph) {
        metrics.start(); // start timing

        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();

        for (int node : graph.getVertices()) {
            if (!visited.contains(node)) {
                dfs(graph, node, visited, stack);
            }
        }

        metrics.stop(); // stop timing
        List<Integer> result = new ArrayList<>(stack);
        Collections.reverse(result);
        return result;
    }

    private void dfs(Graph graph, int node, Set<Integer> visited, Deque<Integer> stack) {
        visited.add(node);
        metrics.increment("DFS-visits");

        for (int neighbor : graph.getAdjacencyList(node)) {
            metrics.increment("DFS-edges");
            if (!visited.contains(neighbor)) {
                dfs(graph, neighbor, visited, stack);
            }
        }

        stack.push(node);
        metrics.increment("Stack-pushes");
    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    public void printMetrics() {
        System.out.println(metrics);
    }
}