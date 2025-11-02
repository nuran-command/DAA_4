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
    @Test
    void testComplexSCC() {
        Graph g = new Graph();
        g.addEdge(0,1); g.addEdge(1,2); g.addEdge(2,0); // SCC1
        g.addEdge(3,4); g.addEdge(4,5); g.addEdge(5,3); // SCC2
        g.addEdge(2,3); // link between SCCs
        SCCFinder scc = new SCCFinder();
        List<Component> comps = scc.findSCCs(g);
        assertEquals(2, comps.size());
        for (Component c : comps) {
            assertTrue(c.getNodes().size() == 3);
        }
    }
    @Test
    void testSingleNodeSCCs() {
        Graph g = new Graph();
        g.addVertex(0); g.addVertex(1);
        SCCFinder scc = new SCCFinder();
        List<Component> comps = scc.findSCCs(g);
        assertEquals(2, comps.size());
        assertEquals(1, comps.get(0).getNodes().size());
        assertEquals(1, comps.get(1).getNodes().size());
    }
    @Test
    void testCondensationMetrics() {
        Graph g = new Graph();
        g.addEdge(0,1); g.addEdge(1,0);
        Kosaraju k = new Kosaraju();
        List<Component> sccs = k.findSCCs(g);
        CondensationGraphBuilder builder = new CondensationGraphBuilder();
        Graph dag = builder.buildCondensation(g,sccs);
        assertTrue(dag.size() < g.size());
    }
}