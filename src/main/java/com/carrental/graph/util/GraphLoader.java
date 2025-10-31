package com.carrental.graph.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.json.*;

/**
 * Loads graph datasets in JSON format.
 * Supports multiple datasets per file and edge weights.
 *
 * Expected JSON format:
 * {
 *   "datasets": [
 *     {
 *       "name": "small1",
 *       "directed": true,
 *       "n": 6,
 *       "edges": [
 *         {"u": 0, "v": 1, "w": 2},
 *         {"u": 1, "v": 2, "w": 3}
 *       ],
 *       "source": 0,
 *       "weight_model": "edge"
 *     }
 *   ]
 * }
 */
public class GraphLoader {

    public static class LoadedGraph {
        public final Graph graph;
        public final Map<String, Integer> weights;
        public final int source;
        public final String name;

        public LoadedGraph(Graph graph, Map<String, Integer> weights, int source, String name) {
            this.graph = graph;
            this.weights = weights;
            this.source = source;
            this.name = name;
        }
    }

    /** Load all graphs from file (returns list of LoadedGraph). */
    public static List<LoadedGraph> loadAllGraphs(String filePath) {
        try {
            String content = Files.readString(Path.of(filePath));
            JSONObject root = new JSONObject(content);
            JSONArray datasets = root.getJSONArray("datasets");

            List<LoadedGraph> result = new ArrayList<>();

            for (int i = 0; i < datasets.length(); i++) {
                JSONObject data = datasets.getJSONObject(i);
                LoadedGraph loaded = parseDataset(data);
                result.add(loaded);
            }
            return result;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read graph file: " + filePath, e);
        }
    }

    /** Load one graph by name. */
    public static LoadedGraph loadSingleGraph(String filePath, String datasetName) {
        try {
            String content = Files.readString(Path.of(filePath));
            JSONObject root = new JSONObject(content);
            JSONArray datasets = root.getJSONArray("datasets");

            for (int i = 0; i < datasets.length(); i++) {
                JSONObject data = datasets.getJSONObject(i);
                if (data.getString("name").equals(datasetName)) {
                    return parseDataset(data);
                }
            }
            throw new IllegalArgumentException("Dataset not found: " + datasetName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read graph file: " + filePath, e);
        }
    }

    /** Helper to parse one dataset JSON into LoadedGraph. */
    private static LoadedGraph parseDataset(JSONObject data) {
        Graph g = new Graph();
        Map<String, Integer> weights = new HashMap<>();

        JSONArray edges = data.getJSONArray("edges");
        for (int j = 0; j < edges.length(); j++) {
            JSONObject e = edges.getJSONObject(j);
            int u = e.getInt("u");
            int v = e.getInt("v");
            int w = e.has("w") ? e.getInt("w") : 1;
            g.addEdge(u, v);
            weights.put(u + "-" + v, w);
        }

        int source = data.has("source") ? data.getInt("source") : 0;
        String name = data.has("name") ? data.getString("name") : "unnamed";
        return new LoadedGraph(g, weights, source, name);
    }
}