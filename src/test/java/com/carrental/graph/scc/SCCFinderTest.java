package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SCCFinderTest {

    @Test
    public void testSimpleCycle() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);

        SCCFinder scc = new SCCFinder();
        List<Component> comps = scc.findSCCs(g);
        assertEquals(1, comps.size());
        assertEquals(3, comps.get(0).getNodes().size());
    }

    @Test
    public void testDisconnectedGraph() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(2, 3);

        SCCFinder scc = new SCCFinder();
        List<Component> comps = scc.findSCCs(g);
        assertEquals(4, comps.stream().mapToInt(c -> c.getNodes().size()).sum());
    }
}