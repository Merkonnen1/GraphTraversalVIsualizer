package org.example;

public class Node {
    public final int x, y;
    public boolean isObstacle;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.isObstacle = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}