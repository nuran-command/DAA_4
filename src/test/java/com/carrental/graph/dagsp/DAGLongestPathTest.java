package com.carrental.graph.dagsp;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DAGLongestPathTest {

    @Test
    void testLongestPathSimple() {
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

        DAGLongestPath algo = new DAGLongestPath();
        PathResult result = algo.longestPaths(g, 0, weights);

        assertEquals(0, result.getDistance().get(0));
        assertEquals(2, result.getDistance().get(1));
        assertEquals(1, result.getDistance().get(2));
        assertEquals(5, result.getDistance().get(3)); // via 0→1→3

        List<Integer> path = result.reconstructPath(0, 3);
        assertEquals(List.of(0, 1, 3), path);
    }

    @Test
    void testDisconnectedGraph() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addVertex(2);

        Map<String, Integer> weights = Map.of("0-1", 10);

        DAGLongestPath algo = new DAGLongestPath();
        PathResult result = algo.longestPaths(g, 0, weights);

        assertEquals(Integer.MIN_VALUE, result.getDistance().get(2));
        assertTrue(result.reconstructPath(0, 2).isEmpty());
    }
}