package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;

import java.util.*;

/**
 * Kahn's algorithm for topological sorting of DAGs.
 * Computes topological order using in-degree counting and a queue.
 * Detects cycles in the graph and collects metrics for operations.
 */
public class KahnAlgorithm {

    private final Metrics metrics;

    /** Default constructor with TimerMetrics. */
    public KahnAlgorithm() {
        this.metrics = new TimerMetrics();
    }

    /** Constructor with custom Metrics implementation. */
    public KahnAlgorithm(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Performs topological sort on the given graph using Kahn's algorithm.
     * @param graph the DAG to sort
     * @return list of vertices in topological order
     * @throws IllegalStateException if a cycle is detected
     */
    public List<Integer> sort(Graph graph) {
        metrics.start();

        // Map vertex -> in-degree (supports arbitrary vertex IDs)
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int v : graph.getVertices()) inDegree.put(v, 0);
        for (int u : graph.getVertices()) {
            for (int neighbor : graph.getAdjacencyList(u)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
                metrics.increment("edge-count");
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) queue.offer(entry.getKey());
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            metrics.increment("queue-polls");
            topoOrder.add(current);

            for (int neighbor : graph.getAdjacencyList(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                    metrics.increment("queue-offers");
                }
            }
        }

        metrics.stop();

        if (topoOrder.size() != graph.getVertices().size()) {
            throw new IllegalStateException("Cycle detected in the graph");
        }

        return topoOrder;
    }

    public Metrics getMetrics() { return this.metrics; }

    public void printMetrics() { System.out.println(metrics); }
}