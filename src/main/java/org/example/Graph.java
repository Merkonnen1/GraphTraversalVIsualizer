package org.example;
import java.util.*;

public class Graph {
    private Node[][] grid;
    private Node start, target;

    public Graph(int width, int height) {
        grid = new Node[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Node(x, y);
            }
        }
    }

    public Node getStart() { return start; }
    public Node getTarget() { return target; }
    public Node getNode(int x, int y) { return grid[x][y]; }
    public int getWidth() { return grid.length; }
    public int getHeight() { return grid[0].length; }

    public List<Node> reconstructPath(Map<Node, Node> parent, Node target) {
        List<Node> path = new ArrayList<>();
        Node current = target;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    public void setStart(int x, int y) { start = grid[x][y]; }
    public void setTarget(int x, int y) { target = grid[x][y]; }
    public void toggleObstacle(int x, int y) { grid[x][y].isObstacle = !grid[x][y].isObstacle; }
}