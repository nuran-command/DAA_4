package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;

import java.util.*;

public class KahnAlgorithm {

    private final Metrics metrics;

    public KahnAlgorithm() {
        this.metrics = new TimerMetrics();
    }

    public KahnAlgorithm(Metrics metrics) {
        this.metrics = metrics;
    }

    public List<Integer> sort(Graph graph) {
        metrics.start(); // start timing

        int vertices = graph.getVerticesCount();
        int[] inDegree = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            for (int neighbor : graph.getAdjacencyList(i)) {
                inDegree[neighbor]++;
                metrics.increment("Edge-count"); // counting edges
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            metrics.increment("Queue-polls");
            topoOrder.add(current);

            for (int neighbor : graph.getAdjacencyList(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                    metrics.increment("Queue-offers");
                }
            }
        }

        metrics.stop(); // stop timing

        if (topoOrder.size() != vertices) {
            throw new IllegalStateException("Cycle detected in the graph");
        }

        return topoOrder;
    }
    public Metrics getMetrics() {
        return this.metrics;
    }

    public void printMetrics() {
        System.out.println(metrics);
    }
}