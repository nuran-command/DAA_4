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

---

## 6. Results Summary
- 6.1 SCC + Condensation + Topological Sort

- 6.2 DAG Shortest & Longest Paths

---


## 7. Analysis
- SCC (Kosaraju): Highest cost on dense graphs (two DFS passes).
- Tological sort (Kahn): Linear performance, ideal for acyclic structures.
- Shortest/Longest path (DP): Efficient on DAGs; scales linearly.
- Performance bottleneck: Dense graphs with many SCCs.
- Structural effect: More edges → longer DFS phase, but DAG phase remains fast.

---

## 8. Conclusions
- SCC compression simplifies cyclic dependencies.
- Biological ordering ensures valid scheduling sequence.
- Shortest path → earliest task completion.
- Longest path → critical path analysis.
- For acyclic datasets, DP-based shortest/longest path gives best runtime.
- Dense cyclic graphs require SCC preprocessing.

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