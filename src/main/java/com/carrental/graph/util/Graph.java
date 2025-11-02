package com.carrental.graph.util;

import java.util.*;

/**
 * Represents a directed graph using an adjacency list.
 * Provides utility methods for graph construction, traversal, and transformations.
 * Used as the core graph structure for SCC, topological sorting, and DAG shortest/longest path computations.
 */
public class Graph {

    private final Map<Integer, List<Integer>> adj;

    /**
     * Constructs an empty graph.
     */
    public Graph() {
        this.adj = new HashMap<>();
    }

    /**
     * Adds a vertex to the graph. If it already exists, does nothing.
     *
     * @param v the vertex to add
     */
    public void addVertex(int v) {
        adj.putIfAbsent(v, new ArrayList<>());
    }

    /**
     * Adds a directed edge from vertex u to vertex v.
     * Automatically adds u and v as vertices if they are not present.
     *
     * @param u source vertex
     * @param v target vertex
     */
    public void addEdge(int u, int v) {
        addVertex(u);
        addVertex(v);
        adj.get(u).add(v);
    }

    /**
     * Returns the adjacency list of a given vertex.
     *
     * @param v vertex
     * @return list of adjacent vertices; empty if vertex has no outgoing edges or does not exist
     */
    public List<Integer> getAdj(int v) {
        return adj.getOrDefault(v, Collections.emptyList());
    }

    /**
     * Returns a set of all vertices in the graph.
     *
     * @return set of vertices
     */
    public Set<Integer> getVertices() {
        return adj.keySet();
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return vertex count
     */
    public int size() {
        return adj.size();
    }

    /**
     * Returns a new graph that is the transpose of this graph.
     * All edges are reversed.
     *
     * @return transposed graph
     */
    public Graph getTranspose() {
        Graph t = new Graph();
        for (int u : adj.keySet()) {
            for (int v : adj.get(u)) {
                t.addEdge(v, u);
            }
        }
        return t;
    }

    /**
     * Checks if a vertex exists in the graph.
     *
     * @param v vertex to check
     * @return true if vertex exists, false otherwise
     */
    public boolean containsVertex(int v) {
        return adj.containsKey(v);
    }

    /**
     * Returns the total number of edges in the graph.
     *
     * @return edge count
     */
    public int edgeCount() {
        int c = 0;
        for (List<Integer> list : adj.values()) c += list.size();
        return c;
    }

    /**
     * Builds a condensation DAG from a list of strongly connected components (SCCs).
     * Each SCC becomes a single vertex in the DAG.
     *
     * @param components list of SCCs
     * @return a new DAG representing the condensed graph
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

    /**
     * Returns the number of vertices in the graph.
     *
     * @return vertex count
     */
    public int getVerticesCount() {
        return adj.size();
    }

    /**
     * Returns the adjacency list of a given vertex.
     * Alias for getAdj(int).
     *
     * @param v vertex
     * @return list of adjacent vertices
     */
    public List<Integer> getAdjacencyList(int v) {
        return getAdj(v);
    }

    /**
     * Returns all vertices as a sorted list for deterministic iteration.
     *
     * @return sorted list of vertices
     */
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