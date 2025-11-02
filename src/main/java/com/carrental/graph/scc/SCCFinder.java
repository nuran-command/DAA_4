package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;

import java.util.*;

/**
 * Finds Strongly Connected Components (SCCs) in a directed graph using Tarjan's algorithm.
 * Metrics for DFS visits, edges, and stack operations are tracked for instrumentation.
 */
public class SCCFinder {

    private int time;  // timestamp counter for discovery times
    private final Map<Integer, Integer> disc = new HashMap<>();  // discovery times
    private final Map<Integer, Integer> low = new HashMap<>();   // low-link values
    private final Deque<Integer> stack = new ArrayDeque<>();     // stack of nodes in current SCC
    private final Set<Integer> onStack = new HashSet<>();        // set of nodes in stack
    private final List<Component> components = new ArrayList<>(); // list of SCCs
    private final Metrics metrics;

    /** Default constructor with TimerMetrics. */
    public SCCFinder() {
        this.metrics = new TimerMetrics();
    }

    /** Constructor with custom metrics implementation. */
    public SCCFinder(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Finds SCCs in the given graph.
     *
     * @param g input directed graph
     * @return list of strongly connected components (Component objects)
     */
    public List<Component> findSCCs(Graph g) {
        metrics.start();
        time = 0;
        for (int v : g.getVertices()) {
            if (!disc.containsKey(v)) {
                dfs(g, v);
            }
        }
        metrics.stop();
        return components;
    }

    /**
     * Recursive DFS for Tarjan's algorithm.
     *
     * @param g graph
     * @param u current node
     */
    private void dfs(Graph g, int u) {
        disc.put(u, time);
        low.put(u, time);
        time++;

        stack.push(u);
        onStack.add(u);

        metrics.increment("DFS-visits");
        metrics.increment("Stack-pushes");

        for (int v : g.getAdj(u)) {
            metrics.increment("DFS-edges");
            if (!disc.containsKey(v)) {
                dfs(g, v);
                low.put(u, Math.min(low.get(u), low.get(v)));
            } else if (onStack.contains(v)) {
                low.put(u, Math.min(low.get(u), disc.get(v)));
            }
        }

        // Root node detected, pop SCC
        if (low.get(u).equals(disc.get(u))) {
            List<Integer> componentNodes = new ArrayList<>();
            int node;
            do {
                node = stack.pop();
                onStack.remove(node);
                componentNodes.add(node);
                metrics.increment("Stack-pops");
            } while (node != u);
            // Sort nodes to make output deterministic for tests
            Collections.sort(componentNodes);
            components.add(new Component(components.size(), componentNodes));
        }
    }

    public Metrics getMetrics() { return metrics; }
    public void printMetrics() { System.out.println(metrics); }
}