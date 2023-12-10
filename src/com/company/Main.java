package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        int width = 800, height = 800;
        int gridWidth = 34, gridHeight = 30;
        Grid grid = new Grid(gridWidth, gridHeight, 20);

        JFrame frame = new JFrame("Test");
        AStarVisualPanel window = new AStarVisualPanel(width, height, grid);
        frame.add(window);
        // Window setup
        frame.setSize(width, height);
        frame.setTitle("A* (AStar)");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Background
        frame.getContentPane().setBackground(Color.darkGray);

        window.setVisible(true);
        frame.setVisible(true);
    }
}
