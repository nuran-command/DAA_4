package com.carrental.graph.topo;

import com.carrental.graph.util.Graph;
import java.util.*;

public class TopologicalSorter {

    public List<Integer> sort(Graph graph) {
        int vertices = graph.getVerticesCount();
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                dfs(graph, i, visited, stack);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private void dfs(Graph graph, int node, boolean[] visited, Stack<Integer> stack) {
        visited[node] = true;
        for (int neighbor : graph.getAdjacencyList(node)) {
            if (!visited[neighbor]) {
                dfs(graph, neighbor, visited, stack);
            }
        }
        stack.push(node);
    }
}