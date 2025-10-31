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
Implemented via TimerMetrics (in com.carrental.graph.util):
•	Timing: System.nanoTime()
•	Counters:
•	SCC → DFS visits, edges processed
•	Topological sort → queue pushes/pops
•	DAG shortest path → relaxations

---
## 10. Testing

JUnit tests in src/test/java verify:
•	SCC grouping correctness
•	Valid topological ordering
•	Correct shortest and longest path reconstruction
•	Edge cases: single-node graph, all-cyclic graph, empty graph

## 11. References

1. GeeksforGeeks – Kosaraju’s Algorithm for Strongly Connected Components:  
   (https://www.geeksforgeeks.org/strongly-connected-components/)

2. GeeksforGeeks – Topological Sorting (Kahn’s Algorithm & DFS):  
   (https://www.geeksforgeeks.org/topological-sorting/)

3. GeeksforGeeks – Shortest Path in Directed Acyclic Graph:  
   (https://www.geeksforgeeks.org/dsa/shortest-path-for-directed-acyclic-graphs/)

4. GeeksforGeeks – Longest Path in Directed Acyclic Graph:  
   (https://www.geeksforgeeks.org/dsa/longest-path-in-a-directed-acyclic-graph-dynamic-programming/)

