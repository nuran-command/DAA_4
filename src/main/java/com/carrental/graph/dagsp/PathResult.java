package com.carrental.graph.dagsp;

import java.util.*;

public class PathResult {
    private final Map<Integer, Integer> distance;
    private final Map<Integer, Integer> parent;

    public PathResult(Map<Integer, Integer> distance, Map<Integer, Integer> parent) {
        this.distance = distance;
        this.parent = parent;
    }

    public Map<Integer, Integer> getDistance() {
        return distance;
    }

    public Map<Integer, Integer> getParent() {
        return parent;
    }

    public List<Integer> reconstructPath(int source, int target) {
        List<Integer> path = new ArrayList<>();
        Integer current = target;
        while (current != null && current != source) {
            path.add(current);
            current = parent.get(current);
        }
        if (current == null) return Collections.emptyList();
        path.add(source);
        Collections.reverse(path);
        return path;
    }
}