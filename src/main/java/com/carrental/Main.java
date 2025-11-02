package com.carrental;

import com.carrental.graph.scc.*;
import com.carrental.graph.topo.*;
import com.carrental.graph.dagsp.*;
import com.carrental.graph.util.*;

import org.json.*;
import java.nio.file.*;
import java.io.IOException;
import java.io.FileWriter;
import java.util.*;

/**
 * Unified Main for Smart City Graph Analysis (SCC + Topological + Shortest/Longest Paths)
 * Processes small, medium, and large datasets, prints metrics, and saves JSON results.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== SMART CITY GRAPH ANALYSIS PIPELINE ===");

        // Datasets: small, medium, large
        String[] datasets = {"small", "medium", "large"};

        for (String datasetName : datasets) {
            String inputPath = "data/" + datasetName + ".json";
            String outputPath = "output/results_" + datasetName + ".json";
            System.out.println("\n=== Processing dataset: " + datasetName + " ===");

            // Ensure output directory exists
            try {
                Files.createDirectories(Path.of("output"));
            } catch (IOException e) {
                System.err.println("⚠️ Failed to create output directory: " + e.getMessage());
            }

            JSONArray allGraphResults = new JSONArray();
            List<GraphLoader.LoadedGraph> graphs = GraphLoader.loadAllGraphs(inputPath);
            System.out.println("Loaded " + graphs.size() + " graphs from " + inputPath);

            for (GraphLoader.LoadedGraph loaded : graphs) {
                String graphName = loaded.name;
                Graph g = loaded.graph;
                System.out.println("\n--- Processing graph: " + graphName + " ---");
                System.out.printf("Vertices: %d, Edges: %d%n", g.getVerticesCount(), g.edgeCount());

                JSONObject graphResult = new JSONObject();
                graphResult.put("graph", graphName);

                // === STEP 1: SCC Detection ===
                Kosaraju kosaraju = new Kosaraju(new TimerMetrics());
                List<Component> sccs = kosaraju.findSCCs(g);
                System.out.println("Detected " + sccs.size() + " strongly connected components.");
                kosaraju.printMetrics();

                // === STEP 2: Compress SCCs into DAG ===
                Graph dag = g.compressSCCs(sccs);
                System.out.printf("Condensed DAG: %d vertices, %d edges%n",
                        dag.getVerticesCount(), dag.edgeCount());

                // === STEP 3: Topological Sort ===
                TopologicalSorter topo = new TopologicalSorter();
                List<Integer> topoOrder = topo.sort(dag);
                System.out.println("Topological order length: " + topoOrder.size());
                topo.printMetrics();

                // === STEP 4: DAG Shortest Paths ===
                DAGShortestPath dagSP = new DAGShortestPath(new TimerMetrics());
                PathResult shortest = dagSP.shortestPaths(dag, loaded.source, loaded.weights);
                dagSP.printMetrics();

                // === STEP 5: DAG Longest Paths ===
                DAGLongestPath dagLP = new DAGLongestPath(new TimerMetrics());
                PathResult longest = dagLP.longestPaths(dag, loaded.source, loaded.weights);
                dagLP.printMetrics();

                // === STEP 6: Collect Metrics ===
                JSONObject metricsJson = new JSONObject();
                metricsJson.put("Kosaraju_SCC", kosaraju.getMetrics().toString());
                metricsJson.put("Topological_Sort", topo.getMetrics().toString());
                metricsJson.put("DAG_Shortest", dagSP.getMetrics().toString());
                metricsJson.put("DAG_Longest", dagLP.getMetrics().toString());

                graphResult.put("scc_count", sccs.size());
                graphResult.put("metrics", metricsJson);
                graphResult.put("shortest_summary", pathSummary(shortest));
                graphResult.put("longest_summary", pathSummary(longest));

                allGraphResults.put(graphResult);
                System.out.println("✅ Graph " + graphName + " processed.");
            }

            // === STEP 7: Save results ===
            saveResults(outputPath, datasetName, allGraphResults);
            System.out.println("💾 Saved dataset results to: " + outputPath);
        }

        System.out.println("\n=== PIPELINE COMPLETE FOR ALL DATASETS ===");
    }

    private static JSONObject pathSummary(PathResult res) {
        JSONObject obj = new JSONObject();
        obj.put("best_distance", res.getBestDistance());
        obj.put("average_distance", res.getAverageDistance());
        obj.put("reachable_nodes", res.getReachableCount());
        obj.put("best_path", res.getBestPath());
        return obj;
    }

    private static void saveResults(String path, String datasetName, JSONArray resultsArray) {
        try {
            JSONObject wrapper = new JSONObject();
            wrapper.put("dataset", datasetName);
            wrapper.put("graphs", resultsArray);

            try (FileWriter writer = new FileWriter(path)) {
                writer.write(wrapper.toString(4));
            }
        } catch (IOException e) {
            System.err.println("⚠️ Failed to save JSON: " + e.getMessage());
        }
    }
}