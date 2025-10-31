package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import com.carrental.graph.util.Metrics;
import com.carrental.graph.util.TimerMetrics;

import java.util.*;

public class SCCFinder {

    private int time;
    private final Map<Integer, Integer> disc = new HashMap<>();
    private final Map<Integer, Integer> low = new HashMap<>();
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Set<Integer> onStack = new HashSet<>();
    private final List<Component> components = new ArrayList<>();
    private final Metrics metrics; // metrics instance

    public SCCFinder() {
        this.metrics = new TimerMetrics();
    }

    public SCCFinder(Metrics metrics) {
        this.metrics = metrics;
    }

    public List<Component> findSCCs(Graph g) {
        metrics.start(); // start timing
        time = 0;
        for (int v : g.getVertices()) {
            if (!disc.containsKey(v)) {
                dfs(g, v);
            }
        }
        metrics.stop(); // stop timing
        return components;
    }

    private void dfs(Graph g, int u) {
        disc.put(u, time);
        low.put(u, time);
        time++;

        stack.push(u);
        onStack.add(u);

        metrics.increment("DFS-visits");
        metrics.increment("Stack-pushes");

        for (int v : g.getAdj(u)) {
            metrics.increment("DFS-edges"); // counting edges explored
            if (!disc.containsKey(v)) {
                dfs(g, v);
                low.put(u, Math.min(low.get(u), low.get(v)));
            } else if (onStack.contains(v)) {
                low.put(u, Math.min(low.get(u), disc.get(v)));
            }
        }

        // root node found
        if (low.get(u).equals(disc.get(u))) {
            List<Integer> component = new ArrayList<>();
            int node;
            do {
                node = stack.pop();
                onStack.remove(node);
                component.add(node);
                metrics.increment("Stack-pops");
            } while (node != u);
            components.add(new Component(components.size(), component));
        }
    }
    public Metrics getMetrics() {
        return this.metrics;
    }

    public void printMetrics() {
        System.out.println(metrics);
    }
}