package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import java.util.*;

/**
 * Builds the condensation DAG of a directed graph given its SCCs.
 * Each SCC is a vertex, and edges are added if original graph has edges between SCCs.
 */
public class CondensationGraphBuilder {

    /**
     * Builds a condensation DAG from a graph and its SCCs.
     *
     * @param g original directed graph
     * @param sccs list of SCCs (components)
     * @return DAG where each vertex is an SCC
     */
    public Graph buildCondensation(Graph g, List<Component> sccs) {
        // Map each original node to its SCC ID
        Map<Integer, Integer> nodeToComp = new HashMap<>();
        for (Component c : sccs) {
            for (int node : c.getNodes()) {
                nodeToComp.put(node, c.getId());
            }
        }

        // Create DAG with one vertex per SCC
        Graph dag = new Graph();
        for (Component c : sccs) {
            dag.addVertex(c.getId());
        }

        // Add edges between SCCs if original graph had edges between nodes of different SCCs
        for (int u : g.getVertices()) {
            for (int v : g.getAdj(u)) {
                int cu = nodeToComp.get(u);
                int cv = nodeToComp.get(v);
                if (cu != cv) {
                    dag.addEdge(cu, cv);
                }
            }
        }
        return dag;
    }
}