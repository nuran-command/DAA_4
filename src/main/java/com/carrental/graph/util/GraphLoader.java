package com.carrental.graph.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;


public class GraphLoader {

    public static Graph loadGraph(String filePath) {
        try {
            String content = Files.readString(Path.of(filePath));
            JSONObject json = new JSONObject(content);
            Graph g = new Graph();

            JSONArray edges = json.getJSONArray("edges");
            for (int i = 0; i < edges.length(); i++) {
                JSONObject e = edges.getJSONObject(i);
                int from = e.getInt("from");
                int to = e.getInt("to");
                g.addEdge(from, to);
            }
            return g;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read graph file: " + filePath, e);
        }
    }
}