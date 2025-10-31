package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import java.util.*;

public class Kosaraju {

    public List<Component> findSCCs(Graph g) {
        // Step 1: normal DFS to get finish order
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> order = new ArrayDeque<>();
        for (int v : g.getVertices()) {
            if (!visited.contains(v)) {
                dfs1(g, v, visited, order);
            }
        }

        // Step 2: transpose graph
        Graph gt = g.getTranspose();

        // Step 3: DFS on transposed graph in reverse finish order
        visited.clear();
        List<Component> components = new ArrayList<>();
        while (!order.isEmpty()) {
            int v = order.pop();
            if (!visited.contains(v)) {
                List<Integer> compNodes = new ArrayList<>();
                dfs2(gt, v, visited, compNodes);
                components.add(new Component(components.size(), compNodes));
            }
        }
        return components;
    }

    private void dfs1(Graph g, int u, Set<Integer> visited, Deque<Integer> order) {
        visited.add(u);
        for (int v : g.getAdj(u)) {
            if (!visited.contains(v)) {
                dfs1(g, v, visited, order);
            }
        }
        order.push(u);
    }

    private void dfs2(Graph g, int u, Set<Integer> visited, List<Integer> compNodes) {
        visited.add(u);
        compNodes.add(u);
        for (int v : g.getAdj(u)) {
            if (!visited.contains(v)) {
                dfs2(g, v, visited, compNodes);
            }
        }
    }
}