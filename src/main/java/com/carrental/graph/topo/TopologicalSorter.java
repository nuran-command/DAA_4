package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;

import java.util.*;

/**
 * DFS-based topological sorter for directed acyclic graphs (DAGs).
 * Computes topological order of graph vertices using depth-first search.
 * Metrics are collected for DFS visits, edges traversed, and stack operations.
 */
public class TopologicalSorter {

    private final Metrics metrics;

    /** Default constructor with TimerMetrics. */
    public TopologicalSorter() {
        this.metrics = new TimerMetrics();
    }

    /** Constructor with custom Metrics implementation. */
    public TopologicalSorter(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Performs topological sort on the given graph using DFS.
     * @param graph the DAG to sort
     * @return list of vertices in topological order
     */
    public List<Integer> sort(Graph graph) {
        metrics.start();

        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();

        // Visit each vertex in the graph
        for (int node : graph.getVertices()) {
            if (!visited.contains(node)) {
                dfs(graph, node, visited, stack);
            }
        }

        metrics.stop();

        // Reverse stack to get correct topological order
        List<Integer> result = new ArrayList<>(stack);
        Collections.reverse(result);
        return result;
    }

    /**
     * Depth-first search to explore vertices and compute finish order.
     * @param graph the DAG
     * @param node current vertex
     * @param visited set of visited vertices
     * @param stack DFS finish stack
     */
    private void dfs(Graph graph, int node, Set<Integer> visited, Deque<Integer> stack) {
        visited.add(node);
        metrics.increment("dfs-visits");

        // Explore all outgoing edges
        for (int neighbor : graph.getAdjacencyList(node)) {
            metrics.increment("dfs-edges");
            if (!visited.contains(neighbor)) {
                dfs(graph, neighbor, visited, stack);
            }
        }

        stack.push(node);
        metrics.increment("stack-pushes");
    }

    public Metrics getMetrics() { return this.metrics; }

    public void printMetrics() { System.out.println(metrics); }
}