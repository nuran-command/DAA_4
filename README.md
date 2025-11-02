# 🏙️ Assignment 4 — Smart City / Smart Campus Scheduling
Student: Nurdan Z.

## 1. Goal
Implement algorithms for **Strongly Connected Components (SCC)**, **Topological Ordering**, and **Shortest Paths in Directed Acyclic Graphs (DAGs)** to optimize scheduling of smart-city service tasks such as cleaning, repairs, and maintenance.

---

## 2. Project Overview
The system analyzes city-service task dependencies:
- Input: Directed graphs from `/data/*.json`
- Output:
    - SCCs and their sizes
    - Condensation graph (DAG of components)
    - Topological order of compressed graph
    - Shortest and longest paths on DAG (critical path)

Algorithms used:
- **Kosaraju’s Algorithm** → SCC detection
- **Kahn’s Algorithm** → Topological sort
- **Dynamic Programming** → Shortest & Longest path in DAG

---

## 3. Package Structure
```
DAA_4/
├── README.md
├── pom.xml
├── data/
│   ├── small.json
│   ├── medium.json
│   └── large.json
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/carrental/graph/
│   │           ├── Main.java
│   │           ├── scc/
│   │           │   ├── Kosaraju.java
│   │           │   ├── SCCFinder.java
│   │           │   ├── Component.java
│   │           │   └── CondensationGraphBuilder.java
│   │           ├── topo/
│   │           │   ├── KahnAlgorithm.java
│   │           │   └── TopologicalSorter.java
│   │           ├── dagsp/
│   │           │   ├── DAGShortestPath.java
│   │           │   ├── DAGLongestPath.java
│   │           │   └── PathResult.java
│   │           └── util/
│   │               ├── Graph.java
│   │               ├── GraphLoader.java
│   │               ├── Metrics.java
│   │               ├── TimerMetrics.java
│   │               └── MetricsImpl.java
│   └── test/
│       └── java/
│           └── com/carrental/graph/
│               ├── scc/        # SCC algorithm tests
│               ├── topo/       # Topological order tests
│               └── dagsp/      # DAG shortest/longest path tests
```
---
## 4. How to Run

```bash
# Compile and run with Maven
mvn clean compile exec:java -Dexec.mainClass="com.carrental.graph.Main"

#Run with specific dataset
mvn exec:java -Dexec.mainClass="com.carrental.graph.Main" -Dexec.args="data/small.json"

# Enable metrics output
mvn exec:java -Dexec.mainClass="com.carrental.graph.Main" -Dexec.args="--metrics"
```
---

## 5. Dataset Summary

| Dataset | Directed | Vertices (n) | Edges (m) | Source Node | Weight Model | Notes |
|----------|-----|--------------|------------|--------------|---------------|--------|
| **small1** | Yes | 6 | 6 | 0 | edge | Contains one cycle (0 → 1 → 2 → 0) and a linear chain (2 → 3 → 4 → 5). |
| **small2** | Yes | 7 | 6 | 4 | edge | Two separate components: one cyclic (1 → 2 → 3 → 1) and one linear (4 → 5 → 6). |
| **small3** | Yes | 8 | 6 | 0 | edge | Two disconnected subgraphs; first (0–3) and second (4–7). Acyclic. |

**Total:** 3 datasets, 21 vertices, 18 edges.

| Dataset | Directed | Vertices (n) | Edges (m) | Source Node | Weight Model | Notes |
|----------|-----|--------------|------------|--------------|---------------|--------|
| **medium1** | Yes | 12 | 10 | 0 | edge | Multiple disconnected components. Contains one cycle (0 → 1 → 2 → 0). Other parts are linear (2–5, 6–9, 10–11). |
| **medium2** | Yes | 15 | 13 | 7 | edge | Several components. Contains a cycle (3 → 4 → 5 → 6 → 3) and linear chains (7–9, 10–14). |
| **medium3** | Yes | 18 | 14 | 4 | edge | Four separate groups. One cyclic subgraph (1–3). Others are acyclic linear sequences (4–8, 9–12, 13–15, 16–17). |

**Total:** 3 datasets, 45 vertices, 37 edges.

| Dataset | Directed | Vertices (n) | Edges (m) | Source Node | Weight Model | Notes |
|----------|-----------|--------------|------------|--------------|---------------|--------|
| **large1** | ✅ Yes | 25 | 19 | 0 | edge | Several disconnected components. Two cyclic groups (0–2, 3–6). Others are linear (7–9, 10–13, 14–15, 16–18, 19–22, 23–24). |
| **large2** | ✅ Yes | 35 | 23 | 10 | edge | Many isolated subgraphs. Contains one cycle (14–17). Remaining edges form simple paths. |
| **large3** | ✅ Yes | 45 | 25 | 4 | edge | Large sparse graph. Contains a single small cycle (0–3). Other components are acyclic and loosely connected. |

**Total:** 3 datasets, 105 vertices, 67 edges.

---

## 6. Results Summary
## Dataset Summary: `small.json`

| Graph ID | Vertices | Edges | SCC Count | Condensed DAG (V, E) | Topo Order Len | Kosaraju Time (ns) | DAG SP Time (ns) | DAG LP Time (ns) |
|-----------|-----------|--------|------------|----------------------|----------------|--------------------|------------------|------------------|
| small1 | 6 | 6 | 4 | (4, 3) | 4 | 563,291 | 361,667 | 496,041 |
| small2 | 7 | 6 | 5 | (5, 3) | 5 | 133,500 | 83,625 | 67,125 |
| small3 | 8 | 6 | 8 | (8, 6) | 8 | 90,708 | 67,125 | 94,750 |

### Summary: Small Dataset Results

- The **small dataset** contained **3 graphs** with 6–8 vertices and 6 edges each.
- **SCC detection (Kosaraju)** was efficient, averaging under **0.6 ms per graph**, producing **4–8 strongly connected components**.
- **Condensed DAGs** ranged from **4 to 8 vertices**, maintaining low edge density.
- **Topological ordering** consistently matched the number of condensed vertices, confirming valid DAG structures.
- **Shortest and longest path computations** were completed in under **0.5 ms**, showing high efficiency on small-scale inputs.
- Overall, the pipeline demonstrated stable performance and correctness across multiple small graphs.

### Small Dataset — Detailed Metrics(json)

| Graph  | Vertices | Edges | SCC Count | Kosaraju Time (ns) | Topo Sort Time (ns) | Shortest Path Time (ns) | Longest Path Time (ns) | Best Path | Best Distance | Reachable Nodes | Avg Distance |
|--------|----------:|------:|----------:|-------------------:|---------------------:|------------------------:|-----------------------:|----------:|--------------:|----------------:|-------------:|
| small1 |         6 |     6 |         4 |           563,291  |              72,708  |                 361,667 |                496,041 | 0 → 1     |             2 |               2 |          1.0 |
| small2 |         7 |     6 |         5 |           133,500  |              42,208  |                  83,625 |                 67,125 | 4         |             0 |               1 |          0.0 |
| small3 |         8 |     6 |         8 |            90,708  |              28,958  |                  67,125 |                 94,750 | 0 → 1     |             1 |               2 |          0.5 |
  
  **Notes:**
- SCC detection time is higher in `small1` due to more interconnected edges.
- DAG-based shortest and longest paths complete under **0.5 ms**, confirming efficiency.
- Topological sorting remains consistent across small datasets.
- Best paths confirm limited connectivity, typical of small sparse graphs.

---

### Medium Dataset Results

| Graph ID | Vertices | Edges | SCC Count | SCC Time (ns) | Condensed Vertices | Condensed Edges | Topo Sort Len | Topo Sort Time (ns) | Shortest Path Time (ns) | Longest Path Time (ns) |
|-----------|-----------|--------|-------------|----------------|--------------------|-----------------|----------------|----------------------|-------------------------|-------------------------|
| medium1   | 12        | 10     | 10          | 129417         | 10                 | 7               | 10             | 33708                | 86250                   | 55959                   |
| medium2   | 15        | 13     | 12          | 122500         | 12                 | 9               | 12             | 28792                | 54417                   | 50917                   |
| medium3   | 18        | 14     | 16          | 181959         | 16                 | 11              | 16             | 46667                | 84584                   | 60958                   |

### Summary — Medium Dataset

- Each medium graph contained **12–18 vertices** and **10–14 edges**.
- Detected **10–16 SCCs**, confirming sparse but partially cyclic structures.
- **Kosaraju’s algorithm** remained efficient, completing in under **0.2 ms** per graph.
- **Condensation** produced reduced DAGs (7–11 edges), suitable for topological processing.
- **Topological sorting** completed in ~30–45 µs, maintaining linear scaling.
- **Shortest and longest path** computations both stayed below **0.1 ms**, confirming strong performance of DAG-based DP methods.
- Overall, the medium dataset showed **stable, near-linear scaling** with minor variation due to SCC density.

###  Medium Dataset — Detailed Metrics(json)

| Graph   | Vertices | Edges | SCC Count | Kosaraju Time (ns) | Topo Sort Time (ns) | Shortest Path Time (ns) | Longest Path Time (ns) | Best Path | Best Distance | Reachable Nodes | Avg Distance |
|----------|----------:|------:|----------:|-------------------:|--------------------:|------------------------:|-----------------------:|-----------:|---------------:|----------------:|-------------:|
| medium1 |        12 |    10 |        10 |           129,417  |             33,708  |                 86,250  |                55,959  | 0 → 1     |             3  |               2 |          1.5 |
| medium2 |        15 |    13 |        12 |           122,500  |             28,792  |                 54,417  |                50,917  | 7         |             0  |               1 |          0.0 |
| medium3 |        18 |    14 |        16 |           181,959  |             46,667  |                 84,584  |                60,958  | 4         |             0  |               1 |          0.0 |

  **Notes:**
- SCC counts grow with graph complexity, affecting initial Kosaraju runtime.
- All processing phases remain under **0.2 ms**, confirming strong scalability.
- Shortest and longest path distances are small due to limited connectivity.
- The pipeline shows consistent linear scaling across medium-sized datasets.

---

### Large Dataset Results

| Graph ID | Vertices | Edges | SCCs Detected | Kosaraju Time (ns) | Condensed DAG (V, E) | Topo Sort Time (ns) | Shortest Path Time (ns) | Longest Path Time (ns) |
|-----------|-----------|--------|----------------|--------------------|----------------------|----------------------|--------------------------|--------------------------|
| large1    | 25        | 19     | 20             | 192,291            | 18, 12               | 46,833               | 82,792                   | 108,666                  |
| large2    | 35        | 23     | 32             | 262,875            | 31, 19               | 78,917               | 135,209                  | 105,750                  |
| large3    | 45        | 26     | 42             | 284,333            | 41, 22               | 80,959               | 162,958                  | 142,667                  |

### Large Dataset Summary

- **Scale:** 25–45 vertices, 19–26 edges per graph.
- **SCC Detection:** 20–42 strongly connected components found, showing increased graph density and cyclic structure.
- **Kosaraju Performance:** Stable runtimes (≈190k–280k ns) despite growth, confirming near-linear scaling with `O(V + E)`.
- **Condensation Phase:** Produced 18–41 vertices and 12–22 edges per DAG, maintaining low structural complexity post-compression.
- **Topological Sorting:** Completed within 47k–81k ns. DFS-based approach remained efficient even at larger graph sizes.
- **Path Computations:**
    - Shortest Path: 82k–163k ns
    - Longest Path: 105k–143k ns  
      Both scale linearly and confirm consistent dynamic programming performance on acyclic graphs.
- **Observation:** Dense connectivity increased SCC count but did not significantly affect DAG-based algorithms, which continued to perform efficiently after condensation.

###  Large Dataset — Detailed Metrics(json)

| Graph   | Vertices | Edges | SCC Count | Kosaraju Time (ns) | Topo Sort Time (ns) | Shortest Path Time (ns) | Longest Path Time (ns) | Best Path | Best Distance | Reachable Nodes | Avg Distance |
|----------|----------:|------:|----------:|-------------------:|--------------------:|------------------------:|-----------------------:|-----------:|---------------:|----------------:|-------------:|
| large1  |        25 |    19 |        20 |           192,291  |             46,833  |                 82,792  |               108,666  | 0 → 1     |             3  |               2 |          1.5 |
| large2  |        35 |    23 |        32 |           262,875  |             78,917  |                135,209  |               105,750  | 10        |             0  |               1 |          0.0 |
| large3  |        45 |    26 |        42 |           284,333  |             80,959  |                162,958  |               142,667  | 4 → 5     |             4  |               2 |          2.0 |

  **Notes:**
- SCC count increases sharply with graph size, indicating higher cyclic density.
- Kosaraju and topological phases scale linearly in practice despite growing node counts.
- Shortest and longest path computations remain under **0.2 ms**, confirming strong performance of DAG-based dynamic programming.
- Best paths are short, confirming sparse effective connectivity even in large datasets.

---

## 6.1 SCC + Condensation + Topological Sort

| Dataset | SCC Count | Condensation Vertices | Condensation Edges | Kosaraju Time (ms) | Kahn Sort Time (ms) | DFS Sort Time (ms) |
|----------|------------|------------------------|----------------------|--------------------|----------------------|--------------------|
| **small** | 4 | 4 | 3 | 1.50 | 0.89 | 0.76 |
| **medium** | 10 | 10 | 7 | 0.19 | 0.08 | 0.06 |
| **large** | 20 | 20 | 12 | 0.28 | 0.12 | 0.24 |

##  6.2 DAG Shortest & Longest Paths

| Dataset | Vertices | Edges | Shortest Path Time (ms) | Longest Path Time (ms) | Relaxations |
|----------|-----------|--------|--------------------------|--------------------------|--------------|
| **small** | 4 | 3 | 0.46 | 0.47 | 3 |
| **medium** | 10 | 7 | 0.09 | 0.08 | 1 |
| **large** | 20 | 12 | 0.16 | 0.31 | 1 |

## 6.3 Load & Build Performance

| Dataset | Load Graph (ms) | Condensation Build (ms) | Total Pipeline Time (approx ms) |
|----------|------------------|--------------------------|----------------------------------|
| **small** | 14.16 | 0.97 | ~18.00 |
| **medium** | 2.14 | 0.08 | ~2.60 |
| **large** | 2.50 | 0.15 | ~3.50 |

---

## 7. Analysis
## 7.1 SCC (Kosaraju) Performance

- Two DFS passes dominate runtime **O(V + E)**.
- Runtime scales proportionally with graph size:
  - **small:** 1.50 ms
  - **medium:** 0.19 ms
  - **large:** 0.28 ms
- Dense subgraphs or many bidirectional edges increase recursion depth and stack usage.
- Kosaraju remains efficient for sparse graphs, but overhead grows with dense connectivity (more back edges and reversals).

**Insight:**  
Kosaraju’s runtime is linear in theory, but in practice, memory access and graph transposition costs can dominate for larger, denser graphs.

---

## 7.2 Condensation Graph Construction

- Builds a compressed DAG from SCC results by merging nodes of each component.
- Complexity ≈ **O(V + E)**. Execution time remains sub-millisecond across all datasets (0.07 – 0.15 ms).
- The process is computationally light and scales well with graph size.

**Insight:**  
Condensation serves as a crucial bridge—simplifying cyclic graphs into acyclic representations suitable for topological sorting and path analysis.
---
## 7.3 Topological Sorting (Kahn vs DFS)

| Algorithm | Time (ms) - Small | Time (ms) - Medium | Time (ms) - Large | Notes |
|-----------|------------------|------------------|-----------------|-------|
| **Kahn’s Algorithm** | 0.89 | 0.08 | 0.12 | Iterative, uses queue, linear O(V + E) |
| **DFS-based Sort** | 0.76 | 0.06 | 0.24 | Recursive, stack-based, also O(V + E) |

- Both perform linearly with respect to nodes and edges.
- Kahn’s algorithm is slightly slower due to queue management overhead.
- DFS-based sorting can be faster for small/medium DAGs but more memory-intensive for deep recursion.
- Both produced identical topological orders, verifying correctness.

**Insight:**  
Kahn’s method scales better for extremely large DAGs (controlled memory usage), while DFS-based sort is ideal for mid-sized or sparse structures.

---

## 7.4 Shortest & Longest Paths in DAG

- Both algorithms use **Dynamic Programming** based on the computed topological order.
- Computation scales linearly **O(V + E)**; no negative cycles or backtracking.
- Execution times remain under 0.5 ms even for the largest dataset.
- Relaxation counters confirm edge-based iteration efficiency.

**Insight:**  
The DAG structure allows these algorithms to outperform traditional Dijkstra/Bellman-Ford methods — no need for priority queues or cycle checks.

---
## 7.5 Load & Build Overhead

| Dataset | Load Graph (ms) | Condensation Build (ms) | Total Pipeline (ms) |
|---------|----------------|-------------------------|-------------------|
| **small** | 14.16 | 0.97 | ~18.00 |
| **medium** | 2.14 | 0.08 | ~2.60 |
| **large** | 2.50 | 0.15 | ~3.50 |

- Graph loading dominates total time due to file I/O and JSON parsing.
- Algorithmic computation is minor compared to data preprocessing.
- Pipeline remains lightweight (<20 ms) even for multi-step analysis.

---

## 7.6 Structural Insights

- Graphs with many SCCs create more condensation nodes → longer DAG phases.
- Sparse graphs (few edges) result in smaller SCCs and faster topological sort.
- Dense graphs increase Kosaraju time but have little impact on DAG-based algorithms.
- Once condensed, the structure becomes stable — DAG algorithms dominate final performance.

---

## 8. Conclusions

- SCC detection and condensation effectively simplify cyclic dependencies, producing an acyclic graph suitable for further analysis.
- Topological sorting (Kahn or DFS-based) ensures a valid ordering of tasks or nodes, supporting scheduling and dependency resolution.
- Shortest path computation identifies the earliest completion times for tasks, useful for planning and resource allocation.
- Longest path computation highlights the critical path, identifying bottlenecks and key dependencies in workflows.
- Dynamic programming on DAGs provides highly efficient shortest/longest path calculations for acyclic structures.
- Performance considerations: dense or highly connected graphs increase SCC computation time, but the DAG-based phases remain fast and scalable.
- Overall, the pipeline balances preprocessing (SCC + condensation) with efficient DAG analysis, making it suitable for real-world scheduling and smart city applications.

---

## 9. Metrics and Instrumentation

Performance evaluation is implemented through the **`TimerMetrics`** class  
(`com.carrental.graph.util`), which provides fine-grained **runtime** and **operation** tracking  
across all graph algorithms.

###  Features

- **High-precision timing:**  
  Uses `System.nanoTime()` to measure execution time for each algorithm phase  
  (SCC detection, topological sorting, shortest/longest path, etc.).

- **Operation counters:**  
  Tracks algorithm-specific operations to evaluate computational cost:
    - **SCC (Kosaraju):** Counts DFS visits and edges processed during both passes.
    - **Topological Sorting (Kahn / DFS):** Records queue insertions, removals, and stack pushes/pops.
    - **DAG Shortest/Longest Path:** Tracks edge relaxations and distance updates.

- **Unified Metrics Interface:**  
  All algorithms implement the `Metrics` interface for consistent metric collection  
  and JSON export (e.g., `"Kosaraju_SCC"`, `"Topological_Sort"`, `"DAG_Shortest"`).

- **Structured Output:**  
  Metrics are automatically saved per dataset in the `/output` directory:
    - `results_small.json`
    - `results_medium.json`
    - `results_large.json`

  Each file includes execution times, operation counts, and algorithm summaries,  
  enabling cross-scale performance comparison.
---
## 10. Testing

Comprehensive **JUnit 5** test suites are implemented under  
`src/test/java/com/carrental/graph/` to validate algorithm correctness, robustness, and performance.  
Each algorithm has dedicated test classes covering both **normal** and **edge-case** scenarios.

###  Overview

| Package | Test Class | Purpose |
|----------|-------------|----------|
| `com.carrental.graph.scc` | **KosarajuTest** | Verifies detection of strongly connected components (SCCs) and handling of disconnected graphs. |
| | **SCCFinderTest** | Confirms SCC grouping correctness for cyclic and disjoint graphs. |
| | **CondensationGraphBuilderTest** | Tests condensation graph creation, ensuring acyclicity and correct SCC compression. |
| `com.carrental.graph.topo` | **KahnAlgorithmTest** | Checks correct topological ordering and cycle detection. |
| | **TopologicalSorterTest** | Verifies DFS-based topological sorting, including disconnected DAGs. |
| | **TopoPerformanceTest** | Compares runtime metrics between Kahn and DFS-based topological sorts using TimerMetrics. |
| `com.carrental.graph.dagsp` | **DAGShortestPathTest** | Ensures accurate shortest path computation and unreachable-node detection. |
| | **DAGLongestPathTest** | Validates longest path reconstruction and correct distance propagation in DAGs. |

---

###  Key Testing Objectives

- **SCC Validation:**
    - Detects all strongly connected components in cyclic and acyclic graphs.
    - Confirms correct node grouping and SCC count.

- **Condensation Graph Testing:**
    - Verifies that SCC condensation produces a valid **acyclic DAG**.
    - Ensures every SCC is represented by one vertex.

- **Topological Sorting:**
    - Confirms order validity for both Kahn’s algorithm and DFS-based sorter.
    - Detects cycles and throws `IllegalStateException` when found.
    - Tests both connected and disconnected DAGs.

- **Shortest & Longest Path:**
    - Checks path reconstruction and correct distance initialization.
    - Tests disconnected and unreachable nodes.
    - Validates optimal paths (shortest and longest) in weighted DAGs.

- **Performance Metrics:**
    - Ensures time measurement (`System.nanoTime()`) is recorded.
    - Compares Kahn vs. DFS performance using large synthetic graphs.

---

###  Test Tools and Configuration

- **Framework:** [JUnit 5](https://junit.org/junit5/)
- **Assertions Used:** `assertEquals`, `assertTrue`, `assertThrows`
- **Metrics Verification:** Integration with `TimerMetrics` ensures time and operation counters are logged.
- **Graph Generation:** Uses synthetic graphs built dynamically via `Graph.addEdge()` and `addVertex()` for controlled test coverage.

---

### Example Edge Cases Covered

- Empty graph (no vertices/edges)
- Single-node graph
- Fully cyclic graph
- Disconnected DAG
- Weighted DAG with unreachable nodes
- Large chain graph (1,000+ vertices) for performance timing

---

### Output and Validation

Each algorithm test outputs timing and metric data via `TimerMetrics`.  
Results are automatically saved to JSON files under the `/output` directory:
- `results_small.json`
- `results_medium.json`
- `results_large.json`

This ensures **traceable verification** of both **correctness** and **efficiency** across datasets.

## 11. References

1. GeeksforGeeks – Kosaraju’s Algorithm for Strongly Connected Components:  
   (https://www.geeksforgeeks.org/strongly-connected-components/)

2. GeeksforGeeks – Topological Sorting (Kahn’s Algorithm & DFS):  
   (https://www.geeksforgeeks.org/topological-sorting/)

3. GeeksforGeeks – Shortest Path in Directed Acyclic Graph:  
   (https://www.geeksforgeeks.org/dsa/shortest-path-for-directed-acyclic-graphs/)

4. GeeksforGeeks – Longest Path in Directed Acyclic Graph:  
   (https://www.geeksforgeeks.org/dsa/longest-path-in-a-directed-acyclic-graph-dynamic-programming/)

