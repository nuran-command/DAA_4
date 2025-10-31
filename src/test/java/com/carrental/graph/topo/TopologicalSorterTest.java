package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class TopologicalSorterTest {

    @Test
    void testSimpleDAG() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> result = sorter.sort(g);

        assertEquals(4, result.size());
        assertTrue(result.indexOf(0) < result.indexOf(1));
        assertTrue(result.indexOf(1) < result.indexOf(2));
        assertTrue(result.indexOf(2) < result.indexOf(3));
    }

    @Test
    void testDisconnectedDAG() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(2, 3);

        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> order = sorter.sort(g);

        assertEquals(4, order.size());
        assertTrue(order.containsAll(List.of(0, 1, 2, 3)));
    }
}