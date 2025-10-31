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
}