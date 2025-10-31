package com.carrental;

import com.carrental.graph.scc.*;
import com.carrental.graph.topo.*;
import com.carrental.graph.util.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== SMART CITY GRAPH ANALYSIS PIPELINE ===");

        // Datasets: small, medium, large
        String[] datasets = {"small", "medium", "large"};

        for (String name : datasets) {
            String filePath = "data/" + name + ".json";
            System.out.println("\n=== Processing dataset: " + name + " ===");

            // === STEP 1: Load Graph ===
            TimerMetrics loadTimer = new TimerMetrics();
            loadTimer.start();
            GraphLoader.LoadedGraph loaded = GraphLoader.loadSingleGraph(filePath, name + "1");
            Graph g = loaded.graph;
            loadTimer.stop();
            System.out.println("Loaded graph: " + g.getVerticesCount() + " vertices, "
                    + g.edgeCount() + " edges (" + loadTimer.getTime() + " ns)");

            // === STEP 2: Find SCCs ===
            TimerMetrics sccTimer = new TimerMetrics();
            sccTimer.start();
            Kosaraju kosaraju = new Kosaraju();
            List<Component> sccs = kosaraju.findSCCs(g);
            sccTimer.stop();
            System.out.println("Detected " + sccs.size() + " strongly connected components "
                    + "(" + sccTimer.getTime() + " ns)");
            for (Component c : sccs) {
                System.out.println("  " + c);
            }

            // === STEP 3: Build Condensation Graph ===
            TimerMetrics condTimer = new TimerMetrics();
            condTimer.start();
            CondensationGraphBuilder builder = new CondensationGraphBuilder();
            Graph dag = builder.buildCondensation(g, sccs);
            condTimer.stop();
            System.out.println("Condensation DAG built. Vertices: " + dag.getVerticesCount() +
                    ", Edges: " + dag.edgeCount() + " (" + condTimer.getTime() + " ns)");

            // === STEP 4: Topological Sort ===
            TimerMetrics topoTimer = new TimerMetrics();
            topoTimer.start();
            KahnAlgorithm topo = new KahnAlgorithm();
            List<Integer> topoOrder = topo.sort(dag);
            topoTimer.stop();
            System.out.println("Topological order: " + topoOrder +
                    " (" + topoTimer.getTime() + " ns)");

            // === STEP 5: Collect Metrics ===
            MetricsImpl metrics = new MetricsImpl();
            metrics.add("Load Graph", loadTimer.getTime());
            metrics.add("Find SCCs", sccTimer.getTime());
            metrics.add("Build Condensation", condTimer.getTime());
            metrics.add("Topological Sort", topoTimer.getTime());
            metrics.print();

            System.out.println("=== Dataset " + name + " processed successfully ===");
        }

        System.out.println("\n=== PIPELINE COMPLETE FOR ALL DATASETS ===");
    }
}