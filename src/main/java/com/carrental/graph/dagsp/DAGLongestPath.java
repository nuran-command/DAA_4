package com.carrental.graph.dagsp;

import com.carrental.graph.util.Graph;
import com.carrental.graph.topo.TopologicalSorter;
import java.util.*;

public class DAGLongestPath {

    public PathResult longestPaths(Graph graph, int source, Map<String, Integer> weights) {
        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> order = sorter.sort(graph);

        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();

        for (int v : graph.getVertices()) dist.put(v, Integer.MIN_VALUE);
        dist.put(source, 0);

        for (int u : order) {
            if (dist.get(u) == Integer.MIN_VALUE) continue;
            for (int v : graph.getAdj(u)) {
                int w = weights.getOrDefault(u + "-" + v, 1);
                if (dist.get(v) < dist.get(u) + w) {
                    dist.put(v, dist.get(u) + w);
                    parent.put(v, u);
                }
            }
        }

        return new PathResult(dist, parent);
    }
}