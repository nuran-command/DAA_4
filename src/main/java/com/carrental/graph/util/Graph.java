package com.carrental.graph.util;

import java.util.*;

public class Graph {

    private final Map<Integer, List<Integer>> adj;

    public Graph() {
        this.adj = new HashMap<>();
    }

    public void addVertex(int v) {
        adj.putIfAbsent(v, new ArrayList<>());
    }

    public void addEdge(int u, int v) {
        addVertex(u);
        addVertex(v);
        adj.get(u).add(v);
    }

    // --- Existing methods ---
    public List<Integer> getAdj(int v) {
        return adj.getOrDefault(v, Collections.emptyList());
    }

    public Set<Integer> getVertices() {
        return adj.keySet();
    }

    public int size() {
        return adj.size();
    }

    public Graph getTranspose() {
        Graph t = new Graph();
        for (int u : adj.keySet()) {
            for (int v : adj.get(u)) {
                t.addEdge(v, u);
            }
        }
        return t;
    }

    public boolean containsVertex(int v) {
        return adj.containsKey(v);
    }

    public int edgeCount() {
        int c = 0;
        for (List<Integer> list : adj.values()) c += list.size();
        return c;
    }
    /**
     * Builds a new DAG from SCC components.
     * Each strongly connected component (SCC) becomes a single vertex in the new graph.
     */
    public Graph compressSCCs(List<com.carrental.graph.scc.Component> components) {
        Graph dag = new Graph();

        // Map original vertex → component ID
        Map<Integer, Integer> compMap = new HashMap<>();
        for (com.carrental.graph.scc.Component comp : components) {
            for (int v : comp.getNodes()) {
                compMap.put(v, comp.getId());
            }
        }

        // Build DAG edges between components
        for (var entry : adj.entrySet()) {
            int u = entry.getKey();
            for (int v : entry.getValue()) {
                int cu = compMap.get(u);
                int cv = compMap.get(v);
                if (cu != cv) { // only add inter-component edges
                    dag.addEdge(cu, cv);
                }
            }
        }

        return dag;
    }


    /** Returns the number of vertices in the graph. */
    public int getVerticesCount() {
        return adj.size();
    }

    /** Returns adjacency list of given vertex (alias for getAdj). */
    public List<Integer> getAdjacencyList(int v) {
        return getAdj(v);
    }

    /** Returns all vertices as a sorted list (useful for deterministic iteration). */
    public List<Integer> getAllVertices() {
        List<Integer> list = new ArrayList<>(adj.keySet());
        Collections.sort(list);
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var entry : adj.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}