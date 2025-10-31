package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class KahnAlgorithmTest {

    @Test
    void testBasicTopologicalOrder() {
        Graph g = new Graph();
        g.addEdge(5, 2);
        g.addEdge(5, 0);
        g.addEdge(4, 0);
        g.addEdge(4, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        KahnAlgorithm kahn = new KahnAlgorithm();
        List<Integer> order = kahn.sort(g);

        // Verify all nodes included
        assertEquals(6, order.size());
        // Check dependency order
        assertTrue(order.indexOf(5) < order.indexOf(0));
        assertTrue(order.indexOf(5) < order.indexOf(2));
        assertTrue(order.indexOf(2) < order.indexOf(3));
        assertTrue(order.indexOf(3) < order.indexOf(1));
    }

    @Test
    void testCycleDetection() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0); // cycle

        KahnAlgorithm kahn = new KahnAlgorithm();

        assertThrows(IllegalStateException.class, () -> kahn.sort(g));
    }
}