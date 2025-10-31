package com.carrental;

import com.carrental.graph.util.Graph;
import com.carrental.graph.scc.*;
import com.carrental.graph.topo.*;
import com.carrental.graph.dagsp.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        // === 1. Build base graph ===
        Graph g = new Graph();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 4);

        System.out.println("Original Graph:");
        System.out.println(g);

        // === 2. Find Strongly Connected Components (Kosaraju) ===
        Kosaraju kosaraju = new Kosaraju();
        List<Component> sccs = kosaraju.findSCCs(g);
        System.out.println("Strongly Connected Components:");
        for (Component c : sccs) System.out.println(c);

        // === 3. Build Condensation Graph (SCC -> DAG) ===
        CondensationGraphBuilder builder = new CondensationGraphBuilder();
        Graph dag = builder.buildCondensation(g, sccs);
        System.out.println("\nCondensation DAG:");
        System.out.println(dag);

        // === 4. Topological Sort ===
        TopologicalSorter topo = new TopologicalSorter();
        List<Integer> topoOrder = topo.sort(dag);
        System.out.println("Topological Order: " + topoOrder);

        // === 5. Assign weights and run DAG shortest/longest path ===
        Map<String, Integer> weights = new HashMap<>();
        for (int u : dag.getVertices()) {
            for (int v : dag.getAdj(u)) {
                weights.put(u + "-" + v, 1); // default weight 1
            }
        }

        int source = topoOrder.get(0);

        DAGShortestPath shortest = new DAGShortestPath();
        PathResult spResult = shortest.shortestPaths(dag, source, weights);
        System.out.println("\nShortest distances from " + source + ": " + spResult.getDistance());

        DAGLongestPath longest = new DAGLongestPath();
        PathResult lpResult = longest.longestPaths(dag, source, weights);
        System.out.println("Longest distances from " + source + ": " + lpResult.getDistance());
    }
}