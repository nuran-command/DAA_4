package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import java.util.*;

public class KahnAlgorithm {

    public List<Integer> sort(Graph graph) {
        int vertices = graph.getVerticesCount();
        int[] inDegree = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            for (int neighbor : graph.getAdjacencyList(i)) {
                inDegree[neighbor]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            topoOrder.add(current);

            for (int neighbor : graph.getAdjacencyList(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        if (topoOrder.size() != vertices) {
            throw new IllegalStateException("Cycle detected in the graph");
        }

        return topoOrder;
    }
}