package com.carrental.integration;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;
import com.carrental.graph.scc.Kosaraju;
import com.carrental.graph.scc.Component;
import com.carrental.graph.topo.TopologicalSorter;
import com.carrental.graph.dagsp.DAGShortestPath;
import com.carrental.graph.dagsp.DAGLongestPath;
import com.carrental.graph.dagsp.PathResult;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Integration test for the full SCC → Condensation → Topo → DAG-SP pipeline.
 */
public class UnifiedPerformanceTest {

    @Test
    public void testFullPipeline() {
        Graph g = new Graph();
        // Simple cyclic + acyclic structure
        g.addEdge(1, 2);
        g.addEdge(2, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(4, 5);

        // 1) SCC detection
        Kosaraju scc = new Kosaraju(new TimerMetrics());
        List<Component> comps = scc.findSCCs(g);
        assertFalse(comps.isEmpty(), "SCCs should be found");

        // 2) Build condensation DAG
        Graph dag = g.compressSCCs(comps);
        assertTrue(dag.size() > 0, "Condensed DAG should have vertices");

        // 3) Topological order
        TopologicalSorter topo = new TopologicalSorter(new TimerMetrics());
        List<Integer> order = topo.sort(dag);
        assertEquals(new HashSet<>(dag.getVertices()), new HashSet<>(order));

        // 4) Shortest & longest paths on DAG
        Map<String, Integer> weights = new HashMap<>();
        for (int u : dag.getVertices()) {
            for (int v : dag.getAdj(u)) {
                weights.put(u + "-" + v, 1);
            }
        }

        int source = dag.getVertices().iterator().next();

        DAGShortestPath sp = new DAGShortestPath(new TimerMetrics());
        PathResult shortest = sp.shortestPaths(dag, source, weights);
        assertNotNull(shortest);
        assertTrue(sp.getMetrics().getTime() > 0);
        assertTrue(sp.getMetrics().getCount("relaxations") >= 0);

        DAGLongestPath lp = new DAGLongestPath(new TimerMetrics());
        PathResult longest = lp.longestPaths(dag, source, weights);
        assertNotNull(longest);
        assertTrue(lp.getMetrics().getTime() > 0);
        assertTrue(lp.getMetrics().getCount("relaxations") >= 0);

        // 5) Basic sanity: at least one node reachable
        long reachable = shortest.getDistance().values()
                .stream()
                .filter(v -> v < Integer.MAX_VALUE)
                .count();
        assertTrue(reachable > 0);
    }
}