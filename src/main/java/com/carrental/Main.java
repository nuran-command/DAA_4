package com.carrental;

import com.carrental.graph.scc.*;
import com.carrental.graph.topo.*;
import com.carrental.graph.dagsp.*;
import com.carrental.graph.util.*;

import java.nio.file.*;
import org.json.*;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== SMART CITY GRAPH ANALYSIS PIPELINE ===");

        // Datasets: small, medium, large
        String[] datasets = {"small", "medium", "large"};

        for (String name : datasets) {
            String filePath = "data/" + name + ".json";
            System.out.println("\n=== Processing dataset: " + name + " ===");
            // === DATASET SUMMARY ===
            try {
                String content = Files.readString(Path.of(filePath));
                JSONObject root = new JSONObject(content);
                JSONArray datasetsArray = root.getJSONArray("datasets");
                System.out.println("Dataset file: " + filePath);
                System.out.println("Contains " + datasetsArray.length() + " graphs:");

                for (int i = 0; i < datasetsArray.length(); i++) {
                    JSONObject d = datasetsArray.getJSONObject(i);
                    String dname = d.getString("name");
                    int n = d.getInt("n");
                    int ecount = d.getJSONArray("edges").length();
                    boolean directed = d.getBoolean("directed");
                    System.out.printf("  • %s — %d vertices, %d edges, directed=%s%n",
                            dname, n, ecount, directed);
                }
            } catch (IOException e) {
                System.out.println("Failed to read dataset summary for " + name);
            }

            // === STEP 1: Load Graph ===
            TimerMetrics loadTimer = new TimerMetrics();
            loadTimer.start();
            GraphLoader.LoadedGraph loaded = GraphLoader.loadSingleGraph(filePath, name + "1");
            Graph g = loaded.graph;
            loadTimer.stop();
            System.out.println("Loaded graph: " + g.getVerticesCount() + " vertices, "
                    + g.edgeCount() + " edges (" + loadTimer.getTime() + " ns)");

            // === STEP 2: Find SCCs (Kosaraju with metrics) ===
            Kosaraju kosaraju = new Kosaraju();
            List<Component> sccs = kosaraju.findSCCs(g);
            System.out.println("Detected " + sccs.size() + " SCCs");
            for (Component c : sccs) System.out.println("  " + c);
            System.out.print("SCC metrics: "); kosaraju.printMetrics();

            // === STEP 3: Build Condensation Graph ===
            TimerMetrics condTimer = new TimerMetrics();
            condTimer.start();
            CondensationGraphBuilder builder = new CondensationGraphBuilder();
            Graph dag = builder.buildCondensation(g, sccs);
            condTimer.stop();
            System.out.println("Condensation DAG built: " + dag.getVerticesCount() + " vertices, "
                    + dag.edgeCount() + " edges (" + condTimer.getTime() + " ns)");

            // === STEP 4: Topological Sort Comparison ===

            // --- Kahn’s Algorithm ---
            KahnAlgorithm kahn = new KahnAlgorithm();
            List<Integer> kahnOrder = kahn.sort(dag);
            System.out.println("Kahn's topological order: " + kahnOrder);
            System.out.print("Kahn metrics: "); kahn.printMetrics();

            // --- DFS-based Topological Sort ---
            TopologicalSorter dfs = new TopologicalSorter();
            List<Integer> dfsOrder = dfs.sort(dag);
            System.out.println("DFS-based topological order: " + dfsOrder);
            System.out.print("DFS metrics: "); dfs.printMetrics();

            // Optional sanity check
            if (kahnOrder.size() != dfsOrder.size()) {
                System.out.println("⚠️ Warning: Kahn and DFS returned different sizes!");
            }

            // === STEP 5: DAG Shortest Paths ===
            DAGShortestPath dagSP = new DAGShortestPath();
            Map<String, Integer> weights = new HashMap<>();
            for (int u : dag.getVertices())
                for (int v : dag.getAdj(u))
                    weights.put(u + "-" + v, 1); // simple equal weights

            PathResult shortest = dagSP.shortestPaths(dag, kahnOrder.get(0), weights);
            System.out.println("Shortest paths from " + kahnOrder.get(0));
            System.out.print("DAG-SP metrics: "); dagSP.printMetrics();

            // === STEP 6: DAG Longest Paths ===
            DAGLongestPath dagLP = new DAGLongestPath();
            PathResult longest = dagLP.longestPaths(dag, kahnOrder.get(0), weights);
            System.out.println("Longest paths from " + kahnOrder.get(0));
            System.out.print("DAG-LP metrics: "); dagLP.printMetrics();

            // === STEP 7: Summary Metrics Table ===
            MetricsImpl summary = new MetricsImpl();
            summary.add("Load Graph", loadTimer.getTime());
            summary.add("Condensation DAG Build", condTimer.getTime());
            summary.add("SCC Kosaraju Time", kosaraju.getMetrics().getTime());
            summary.add("KahnAlgorithm Time", kahn.getMetrics().getTime());
            summary.add("DFS TopologicalSorter Time", dfs.getMetrics().getTime());
            summary.add("DAG Shortest Path Time", dagSP.getMetrics().getTime());
            summary.add("DAG Longest Path Time", dagLP.getMetrics().getTime());

            System.out.println("\n=== Summary Metrics for dataset " + name + " ===");
            summary.print();

            System.out.println("=== Dataset " + name + " processed successfully ===\n");
        }

        System.out.println("=== PIPELINE COMPLETE FOR ALL DATASETS ===");
    }
}