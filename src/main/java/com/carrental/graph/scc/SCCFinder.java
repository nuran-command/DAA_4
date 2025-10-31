package com.carrental.graph.scc;

import com.carrental.graph.util.Graph;
import java.util.*;

public class SCCFinder {

    private int time;
    private final Map<Integer, Integer> disc = new HashMap<>();
    private final Map<Integer, Integer> low = new HashMap<>();
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Set<Integer> onStack = new HashSet<>();
    private final List<Component> components = new ArrayList<>();

    public List<Component> findSCCs(Graph g) {
        time = 0;
        for (int v : g.getVertices()) {
            if (!disc.containsKey(v)) {
                dfs(g, v);
            }
        }
        return components;
    }

    private void dfs(Graph g, int u) {
        disc.put(u, time);
        low.put(u, time);
        time++;
        stack.push(u);
        onStack.add(u);

        for (int v : g.getAdj(u)) {
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
            } while (node != u);
            components.add(new Component(components.size(), component));
        }
    }
}