package com.carrental.graph.dagsp;

import java.util.*;

public class PathResult {
    private final Map<Integer, Integer> distance;
    private final Map<Integer, Integer> parent;

    private final int bestDistance;         // largest/smallest distance depending on algo
    private final double averageDistance;   // average distance of reachable nodes
    private final int reachableCount;       // how many nodes are reachable
    private final List<Integer> bestPath;   // path to best node

    public PathResult(Map<Integer, Integer> distance, Map<Integer, Integer> parent) {
        this.distance = distance;
        this.parent = parent;

        // compute metrics summary
        int bestNode = -1;
        int best = Integer.MIN_VALUE;
        long sum = 0;
        int count = 0;

        // compute best and average
        for (Map.Entry<Integer, Integer> entry : distance.entrySet()) {
            int d = entry.getValue();
            if (d != Integer.MAX_VALUE && d != Integer.MIN_VALUE) { // reachable
                count++;
                sum += d;
                if (d > best) {
                    best = d;
                    bestNode = entry.getKey();
                }
            }
        }

        this.bestDistance = (count > 0 ? best : 0);
        this.reachableCount = count;
        this.averageDistance = (count > 0 ? (double) sum / count : 0.0);

        // reconstruct path for best node
        this.bestPath = reconstructPathFromBest(bestNode);
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

    private List<Integer> reconstructPathFromBest(int target) {
        if (target == -1) return Collections.emptyList();
        List<Integer> path = new ArrayList<>();
        Integer current = target;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    // === getters for JSON summary ===
    public int getBestDistance() {
        return bestDistance;
    }

    public double getAverageDistance() {
        return averageDistance;
    }

    public int getReachableCount() {
        return reachableCount;
    }

    public List<Integer> getBestPath() {
        return bestPath;
    }
}