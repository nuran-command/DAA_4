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