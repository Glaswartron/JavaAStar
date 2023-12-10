package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashMap;

public class AStarVisualPanel extends JPanel
        implements MouseListener, MouseMotionListener {

    public Grid grid;

    public int width, height;

    public int animationDelay = 40;

    private final int MIN_ANIMATION_DELAY = 0;
    private final int MAX_ANIMATION_DELAY = 100;

    private final int centerX, centerY;

    private final Rectangle[][] cellVisuals;

    private Cell startCell, goalCell;

    private final JPanel panel;
    private final JButton startButton;
    private final JSlider animationSpeedSlider;

    private Cell previouslyClickedCell;

    private boolean running;

    public AStarVisualPanel(int width, int height, Grid grid) {
        this.width = width; this.height = height;
        centerX = width / 2; centerY = height / 2;

        this.grid = grid;

        cellVisuals = new Rectangle[grid.height][grid.width];

        addMouseListener(this);
        addMouseMotionListener(this);

        // Create JButton and JPanel
        startButton = new JButton("Start!");
        startButton.addActionListener(new StartButtonListener());

        animationSpeedSlider = new JSlider(JSlider.HORIZONTAL, MIN_ANIMATION_DELAY, MAX_ANIMATION_DELAY, 40);
        animationSpeedSlider.setMajorTickSpacing(20);
        animationSpeedSlider.setMinorTickSpacing(10);
        animationSpeedSlider.setPaintTicks(true);
        animationSpeedSlider.setPaintLabels(true);
        animationSpeedSlider.addChangeListener
                (e -> animationDelay = animationSpeedSlider.getValue());

        // Add UI elements to JPanel
        panel = new JPanel();
        panel.add(startButton);
        panel.add(new JLabel("Animation Delay:"));
        panel.add(animationSpeedSlider);

        add(panel);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < grid.height; y++) {
            for (int x = 0; x < grid.width; x++) {
                Cell cell = grid.cells[y][x];

                int sideLength = grid.cellSideLength;

                // Draw a centered grid
                int xPos = centerX - ((grid.width / 2) * sideLength) + x * sideLength;
                int yPos = centerY - ((grid.height / 2) * sideLength) + y * sideLength;

                cellVisuals[y][x] = new Rectangle(xPos, yPos, sideLength, sideLength);

                // Determine color of individual cell based on its state
                Color c = Color.magenta;
                switch (cell.state) {
                    case NOT_VISITED:
                        c = Color.lightGray;
                        break;
                    case OPEN:
                        c = Color.gray;
                        break;
                    case CLOSED:
                        c = Color.red;
                        break;
                    case OPTIMAL_PATH:
                        c = Color.green;
                        break;
                    case OBSTACLE:
                        c = Color.BLACK;
                        break;
                    case START:
                        c = Color.blue;
                        break;
                    case GOAL:
                        c = Color.orange;
                        break;
                }

                g.setColor(c);
                g.fillRect(xPos, yPos, sideLength, sideLength);
                g.setColor(Color.black);
            }
        }

        // UI on top
        //paintComponents(g);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e))
            rightMouseButtonClicked(e);
    }

    public void updateWindow() {
        paintComponent(getGraphics());
        panel.paintComponents(panel.getGraphics());
    }

    private void rightMouseButtonClicked(MouseEvent e) {
        Point2D clickPos = e.getPoint();

        // Determine which cell was clicked on
        Cell clickedCell = getCellAtPosition(clickPos);

        if (clickedCell != null) {
            if (clickedCell.state == CellState.START) {
                setGoalCell(clickedCell);
            } else if (clickedCell.state == CellState.GOAL) {
                clickedCell.state = CellState.OPEN;
                goalCell = null;
            } else {
                if (startCell != null)
                    setGoalCell(clickedCell);
                else
                    setStartCell(clickedCell);
            }

            updateWindow();
        }
    }

    private Cell getCellAtPosition(Point2D pos) {
        Cell cell = null;
        for (int y = 0; y < grid.height; y++) {
            for (int x = 0; x < grid.width; x++) {
                if (cellVisuals[y][x].contains(pos))
                    cell = grid.cells[y][x];
            }
        }

        return cell;
    }

    private void setStartCell(Cell cell) {
        if (startCell != null)
            startCell.state = CellState.OPEN;

        startCell = cell;
        cell.state = CellState.START;
    }

    private void setGoalCell(Cell cell) {
        if (goalCell != null) {
            goalCell.state = CellState.OPEN;
        }

        if (cell == startCell)
            startCell = null;

        goalCell = cell;
        cell.state = CellState.GOAL;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK)
                != MouseEvent.BUTTON1_DOWN_MASK)
            return;

        Point2D clickPos = e.getPoint();

        // Determine which cell was clicked on
        Cell clickedCell = getCellAtPosition(clickPos);

        if (previouslyClickedCell != null)
            if (clickedCell != null && clickedCell == previouslyClickedCell)
                return;

        previouslyClickedCell = clickedCell;

        if (clickedCell != null) {
            if (clickedCell.state == CellState.NOT_VISITED)
                clickedCell.state = CellState.OBSTACLE;
            else if (clickedCell.state == CellState.OBSTACLE)
                clickedCell.state = CellState.NOT_VISITED;

            updateWindow();
        }
    }

    class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            running = true;

            try {
                AStar.run(AStarVisualPanel.this, grid.cells, new Point(startCell.x, startCell.y),
                        new Point(goalCell.x, goalCell.y), animationDelay);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            updateWindow();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }

}
