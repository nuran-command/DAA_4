package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TopoPerformanceTest {

    @Test
    void compareTopoSortTimes() {
        Graph g = new Graph();
        for (int i = 0; i < 1000; i++) g.addVertex(i);
        for (int i = 0; i < 999; i++) g.addEdge(i, i + 1);

        KahnAlgorithm kahn = new KahnAlgorithm();
        TopologicalSorter dfs = new TopologicalSorter();

        kahn.sort(g);
        dfs.sort(g);

        assertTrue(kahn.getMetrics().getTime() > 0, "KahnAlgorithm time should be recorded");
        assertTrue(dfs.getMetrics().getTime() > 0, "TopologicalSorter time should be recorded");
    }
}