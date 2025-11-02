package com.carrental.graph.scc;

import java.util.List;

/**
 * Represents a strongly connected component (SCC) in a directed graph.
 * Each component has a unique ID and a list of nodes (vertices) that belong to it.
 */
public class Component {

    /** Unique identifier for this component. */
    private final int id;

    /** List of nodes (vertex IDs) that are part of this component. */
    private final List<Integer> nodes;

    /**
     * Constructs a strongly connected component with the given ID and list of nodes.
     *
     * @param id the unique ID of the component
     * @param nodes the list of vertex IDs contained in this component
     */
    public Component(int id, List<Integer> nodes) {
        this.id = id;
        this.nodes = nodes;
    }

    /**
     * Returns the unique ID of this component.
     *
     * @return the component ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the list of nodes contained in this component.
     *
     * @return a list of vertex IDs
     */
    public List<Integer> getNodes() {
        return nodes;
    }

    /**
     * Returns a string representation of the component, including its ID and nodes.
     *
     * @return string in the format "Component <id>: [nodes]"
     */
    @Override
    public String toString() {
        return "Component " + id + ": " + nodes;
    }
}