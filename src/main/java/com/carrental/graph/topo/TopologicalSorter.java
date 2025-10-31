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

        int vertices = graph.getVerticesCount();
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                dfs(graph, i, visited, stack);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
            metrics.increment("Stack-pops");
        }

        metrics.stop(); // stop timing
        return result;
    }

    private void dfs(Graph graph, int node, boolean[] visited, Stack<Integer> stack) {
        visited[node] = true;
        metrics.increment("DFS-visits");
        for (int neighbor : graph.getAdjacencyList(node)) {
            metrics.increment("DFS-edges");
            if (!visited[neighbor]) {
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