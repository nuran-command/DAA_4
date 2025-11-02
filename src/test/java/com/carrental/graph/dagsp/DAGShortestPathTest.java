package com.carrental.graph.dagsp;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

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
        weights.put("1-3", 2);
        weights.put("2-3", 3);

        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, weights);

        assertEquals(0, result.getDistance().get(0));
        assertEquals(2, result.getDistance().get(1));
        assertEquals(1, result.getDistance().get(2));
        assertEquals(4, result.getDistance().get(3));

        List<Integer> path = result.reconstructPath(0, 3);
        List<List<Integer>> validPaths = List.of(
                List.of(0, 1, 3),
                List.of(0, 2, 3)
        );
        assertTrue(validPaths.contains(path), "Path should be one of the optimal routes");
    }

    @Test
    void testUnreachableNode() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addVertex(2); // isolated

        Map<String, Integer> weights = Map.of("0-1", 5);

        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, weights);

        assertEquals(Integer.MAX_VALUE, result.getDistance().get(2));
        assertTrue(result.reconstructPath(0, 2).isEmpty());
    }
    @Test
    void testEmptyGraph() {
        Graph g = new Graph();
        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, Map.of());
        assertTrue(result.getDistance().isEmpty());
    }

    @Test
    void testSingleNodeGraph() {
        Graph g = new Graph();
        g.addVertex(0);
        DAGLongestPath algo = new DAGLongestPath();
        PathResult result = algo.longestPaths(g, 0, Map.of());
        assertEquals(0, result.getDistance().get(0));
        assertEquals(List.of(0), result.reconstructPath(0, 0));
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
        assertEquals(2,result.getDistance().get(3));
        List<Integer> path = result.reconstructPath(0,3);
        assertTrue(path.equals(List.of(0,1,3)) || path.equals(List.of(0,2,3)));
    }
    @Test
    void testMetricsRecorded() {
        Graph g = new Graph();
        g.addEdge(0,1);
        Map<String,Integer> weights = Map.of("0-1",10);
        DAGShortestPath algo = new DAGShortestPath();
        algo.shortestPaths(g,0,weights);
        assertTrue(algo.getMetrics().getTime() > 0);
        assertTrue(algo.getMetrics().getCount("relaxations") > 0);
    }
    @Test
    void testLargeDAGPerformance() {
        Graph g = new Graph();
        int n = 1000; // medium-large DAG
        for (int i = 0; i < n; i++) g.addVertex(i);
        for (int i = 0; i < n - 1; i++) g.addEdge(i, i + 1); // linear chain

        Map<String,Integer> weights = new HashMap<>();
        for (int i = 0; i < n - 1; i++) weights.put(i + "-" + (i+1), 1);

        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, weights);

        // Check last node distance
        assertEquals(n-1, result.getDistance().get(n-1));
        // Ensure metrics recorded
        assertTrue(algo.getMetrics().getTime() > 0);
        assertTrue(algo.getMetrics().getCount("relaxations") > 0);
    }
    @Test
    void testZeroAndNegativeWeights() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(1, 2);

        Map<String,Integer> weights = Map.of(
                "0-1", 0,    // zero weight
                "1-2", -2    // negative weight (allowed in DAG)
        );

        DAGShortestPath algo = new DAGShortestPath();
        PathResult result = algo.shortestPaths(g, 0, weights);

        assertEquals(0, result.getDistance().get(0));
        assertEquals(0, result.getDistance().get(1));
        assertEquals(-2, result.getDistance().get(2));
    }


}