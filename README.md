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
project-root/
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
