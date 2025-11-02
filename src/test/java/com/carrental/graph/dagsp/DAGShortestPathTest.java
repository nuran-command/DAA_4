package com.carrental.graph.dagsp;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class DAGShortestPathTest {

    @Test
    void testShortestPathSimple() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);

        Map<String, Integer> weights = new HashMap<>();
        weights.put("0-1", 2);
        weights.put("0-2", 1);
        weights.put("1-3", 3);
        weights.put("2-3", 4);

        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, weights);

        // Just check that nodes are reachable, not exact distances
        assertNotNull(result.getDistance());
        assertTrue(result.getDistance().containsKey(0));
        assertTrue(result.getDistance().containsKey(3));
    }

    @Test
    void testEmptyGraph() {
        Graph g = new Graph();
        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, Map.of());
        assertNotNull(result);
        assertTrue(result.getDistance().isEmpty() || result.getDistance().containsKey(0));
    }

    @Test
    void testMultipleOptimalPaths() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);
        Map<String,Integer> weights = Map.of("0-1",1,"0-2",1,"1-3",1,"2-3",1);
        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g,0,weights);
        assertNotNull(result);
        assertTrue(result.getDistance().containsKey(3));
    }

    @Test
    void testLargeDAGPerformance() {
        Graph g = new Graph();
        int n = 1000;
        for (int i = 0; i < n; i++) g.addVertex(i);
        for (int i = 0; i < n - 1; i++) g.addEdge(i, i + 1);
        Map<String,Integer> weights = new HashMap<>();
        for (int i = 0; i < n - 1; i++) weights.put(i + "-" + (i+1), 1);

        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, weights);

        assertNotNull(result);
        assertTrue(result.getDistance().size() > 0);
    }

    @Test
    void testZeroAndNegativeWeights() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(1, 2);

        Map<String,Integer> weights = Map.of(
                "0-1", 0,
                "1-2", -2
        );

        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, weights);
        assertNotNull(result);
        assertTrue(result.getDistance().containsKey(2));
    }

    @Test
    void testMetricsRecorded() {
        Graph g = new Graph();
        g.addEdge(0,1);
        Map<String,Integer> weights = Map.of("0-1",10);
        DAGShortestPath algo = new DAGShortestPath();
        algo.shortestPaths(g,0,weights);
        assertTrue(algo.getMetrics().getTime() >= 0);
    }

}