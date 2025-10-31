package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class CondensationGraphBuilderTest {

    @Test
    public void testCondensationSimple() {

        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 4);

        Kosaraju kosaraju = new Kosaraju();
        List<Component> sccs = kosaraju.findSCCs(g);

        // Build condensation graph
        CondensationGraphBuilder builder = new CondensationGraphBuilder();
        Graph dag = builder.buildCondensation(g, sccs);

        // DAG must be acyclic
        assertTrue(isAcyclic(dag), "Condensed graph must be acyclic");

        // Should have fewer vertices than original graph (compressed SCC)
        assertTrue(dag.size() < g.size(), "Condensation must reduce node count");

        // Check if all components are represented
        Set<Integer> ids = new HashSet<>();
        for (Component c : sccs) ids.add(c.getId());
        for (int v : dag.getVertices()) assertTrue(ids.contains(v));
    }

    private boolean isAcyclic(Graph g) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> stack = new HashSet<>();

        for (int v : g.getVertices()) {
            if (dfsCycle(g, v, visited, stack)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfsCycle(Graph g, int u, Set<Integer> visited, Set<Integer> stack) {
        if (stack.contains(u)) return true;
        if (visited.contains(u)) return false;

        visited.add(u);
        stack.add(u);
        for (int v : g.getAdj(u)) {
            if (dfsCycle(g, v, visited, stack)) return true;
        }
        stack.remove(u);
        return false;
    }
}