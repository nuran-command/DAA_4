package com.carrental.graph.scc;

import java.util.List;

public class Component {
    private final int id;
    private final List<Integer> nodes;

    public Component(int id, List<Integer> nodes) {
        this.id = id;
        this.nodes = nodes;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return "Component " + id + ": " + nodes;
    }
}