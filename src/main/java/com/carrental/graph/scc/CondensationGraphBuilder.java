package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import java.util.*;

public class CondensationGraphBuilder {

    public Graph buildCondensation(Graph g, List<Component> sccs) {
        Map<Integer, Integer> nodeToComp = new HashMap<>();
        for (Component c : sccs) {
            for (int node : c.getNodes()) {
                nodeToComp.put(node, c.getId());
            }
        }

        Graph dag = new Graph();
        for (Component c : sccs) {
            dag.addVertex(c.getId());
        }

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