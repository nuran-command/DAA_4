package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class KosarajuTest {

    @Test
    public void testSimpleSCC() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);

        Kosaraju k = new Kosaraju();
        List<Component> sccs = k.findSCCs(g);

        assertEquals(2, sccs.size(), "Should find 2 SCCs");

        boolean foundCycle = sccs.stream().anyMatch(c -> c.getNodes().containsAll(List.of(0,1,2)));
        assertTrue(foundCycle, "Cycle 0->1->2 detected");
    }

    @Test
    public void testDisconnectedGraph() {
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(2, 3);

        Kosaraju k = new Kosaraju();
        List<Component> sccs = k.findSCCs(g);

        assertEquals(4, sccs.stream().mapToInt(c -> c.getNodes().size()).sum());
    }
}