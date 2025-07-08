package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Visualizer extends JPanel {
    private static final int CELL_SIZE = 30;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 15;
    private static final int ANIMATION_DELAY = 50; // milliseconds between steps
    
    private Graph graph;
    private List<Node> path;
    private List<Node> searchOrder;
    private boolean isSettingStart = true;
    private boolean isSettingTarget = false;
    private int currentSearchStep = 0;
    private Timer animationTimer;
    
    public Visualizer() {
        graph = new Graph(GRID_WIDTH, GRID_HEIGHT);
        setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));
        setupMouseListener();
        setupKeyListener();
        setFocusable(true);
    }
    
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                
                if (x >= 0 && x < GRID_WIDTH && y >= 0 && y < GRID_HEIGHT) {
                    if (isSettingStart) {
                        graph.setStart(x, y);
                        isSettingStart = false;
                        isSettingTarget = true;
                    } else if (isSettingTarget) {
                        graph.setTarget(x, y);
                        isSettingTarget = false;
                    } else {
                        graph.toggleObstacle(x, y);
                    }
                    resetSearch();
                    repaint();
                }
            }
        });
    }
    
    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startSearch();
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    resetSearch();
                    repaint();
                }
            }
        });
    }

    private void resetSearch() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        path = null;
        searchOrder = null;
        currentSearchStep = 0;
    }

    private void startSearch() {
        resetSearch();
        Traversal.SearchResult result = Traversal.bfs(graph);
        searchOrder = result.searchOrder;
        path = result.path;
        
        animationTimer = new Timer(ANIMATION_DELAY, e -> {
            if (currentSearchStep < searchOrder.size()) {
                currentSearchStep++;
                repaint();
            } else if (path != null && !path.isEmpty()) {
                ((Timer)e.getSource()).stop();
            }
        });
        animationTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw grid
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                Node node = graph.getNode(x, y);
                if (node.isObstacle) {
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2d.setColor(Color.GRAY);
                g2d.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Draw searched cells
        if (searchOrder != null) {
            Color searchColor = new Color(173, 216, 230); // Light blue
            for (int i = 0; i < currentSearchStep && i < searchOrder.size(); i++) {
                Node node = searchOrder.get(i);
                g2d.setColor(searchColor);
                g2d.fillRect(node.x * CELL_SIZE + 2, node.y * CELL_SIZE + 2, 
                            CELL_SIZE - 4, CELL_SIZE - 4);
                
                // Draw search order number
                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                String number = String.valueOf(i + 1);
                FontMetrics metrics = g2d.getFontMetrics();
                int textX = node.x * CELL_SIZE + (CELL_SIZE - metrics.stringWidth(number)) / 2;
                int textY = node.y * CELL_SIZE + (CELL_SIZE + metrics.getHeight()) / 2 - 2;
                g2d.drawString(number, textX, textY);
            }
        }
        
        // Draw final path
        if (path != null && currentSearchStep >= searchOrder.size()) {
            g2d.setColor(Color.BLUE);
            for (Node node : path) {
                g2d.fillRect(node.x * CELL_SIZE + 5, node.y * CELL_SIZE + 5, 
                            CELL_SIZE - 10, CELL_SIZE - 10);
            }
        }
        
        // Draw start and target
        if (graph.getStart() != null) {
            g2d.setColor(Color.GREEN);
            Node start = graph.getStart();
            g2d.fillOval(start.x * CELL_SIZE + 5, start.y * CELL_SIZE + 5, 
                        CELL_SIZE - 10, CELL_SIZE - 10);
        }
        
        if (graph.getTarget() != null) {
            g2d.setColor(Color.RED);
            Node target = graph.getTarget();
            g2d.fillOval(target.x * CELL_SIZE + 5, target.y * CELL_SIZE + 5, 
                        CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }
}