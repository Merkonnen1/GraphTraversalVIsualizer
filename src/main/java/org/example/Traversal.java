package org.example;

import java.util.*;

public class Traversal {
    public static class SearchResult {
        public final List<Node> path;
        public final List<Node> searchOrder;

        public SearchResult(List<Node> path, List<Node> searchOrder) {
            this.path = path;
            this.searchOrder = searchOrder;
        }
    }

    public static SearchResult bfs(Graph graph) {
        if (graph.getStart() == null || graph.getTarget() == null) {
            return new SearchResult(new ArrayList<>(), new ArrayList<>());
        }

        Queue<Node> queue = new LinkedList<>();
        Map<Node, Node> parent = new HashMap<>();
        List<Node> searchOrder = new ArrayList<>();
        
        queue.add(graph.getStart());
        parent.put(graph.getStart(), null);
        searchOrder.add(graph.getStart());

        boolean found = false;
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current == graph.getTarget()) {
                found = true;
                break;
            }

            for (Node neighbor : getNeighbors(graph, current)) {
                if (!parent.containsKey(neighbor) && !neighbor.isObstacle) {
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                    searchOrder.add(neighbor);
                }
            }
        }

        List<Node> path = found ? graph.reconstructPath(parent, graph.getTarget()) : new ArrayList<>();
        return new SearchResult(path, searchOrder);
    }

    private static List<Node> getNeighbors(Graph graph, Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {
            {0, 1},  // right
            {1, 0},  // down
            {0, -1}, // left
            {-1, 0}  // up
        };
        
        for (int[] dir : directions) {
            int newX = node.x + dir[0];
            int newY = node.y + dir[1];
            
            if (newX >= 0 && newX < graph.getWidth() && 
                newY >= 0 && newY < graph.getHeight()) {
                neighbors.add(graph.getNode(newX, newY));
            }
        }
        return neighbors;
    }
}