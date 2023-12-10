package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class AStar {

    public static void run(AStarVisualPanel window, Cell[][] grid, Point start, Point end, long animationDelay) throws InterruptedException {

        Cell cell; // The cell to be evaluated

        // All nodes that have been found but not yet been evaluated
        HashSet<Cell> open = new HashSet<>();

        open.add(grid[start.y][start.x]);

        // There is no closed set since closed nodes are marked by their CLOSED state

        while (true) {

            // Get cell with lowest F cost
            cell = null;
            for (Cell c : open) {
                if (cell != null) {
                    if (c.getFcost() < cell.getFcost())
                        cell = c;
                    else if (c.getFcost() == cell.getFcost()) {
                        if (c.hCost < cell.hCost)
                            cell = c;
                    }
                } else cell = c;
            }
            assert cell != null;

            open.remove(cell);
            if (cell.state != CellState.START) cell.state = CellState.CLOSED;

            Cell[] adjCells = getAdjacentCells(grid, cell.x, cell.y);

            for (Cell adjC : adjCells) {

                // End reached
                if (adjC.x == end.x && adjC.y == end.y) {
                    // Trace back path
                    while (true) {

                        if (cell.x == start.x && cell.y == start.y)
                            return;

                        cell.state = CellState.OPTIMAL_PATH;

                        cell = cell.parent;

                        window.updateWindow();
                        if (animationDelay != 0)
                            Thread.sleep(animationDelay);
                    }
                }

                if (adjC.state == CellState.CLOSED || adjC.state == CellState.OBSTACLE
                        || adjC.state == CellState.START)
                    continue;

                // G cost
                int gCost = cell.gCost;
                if (adjC.y != cell.y)
                    gCost += adjC.x == cell.x ? 10 : 14;
                else if (adjC.x != cell.x)
                    gCost += 10;
                // ---

                // H cost
                int dx = Math.abs(end.x - adjC.x);
                int dy = Math.abs(end.y - adjC.y);

                int diag = Math.min(dx, dy);
                int horizOrVert = Math.max(dx, dy) - Math.min(dx, dy);

                int hCost = 14 * diag + 10 * horizOrVert;
                // ---

                if (!open.contains(adjC)) {
                    adjC.gCost = gCost;
                    adjC.hCost = hCost;
                    adjC.parent = cell;

                    open.add(adjC);
                    adjC.state = CellState.OPEN;
                } else {
                        if (Cell.getFcost(gCost, hCost) < adjC.getFcost()) {
                            adjC.gCost = gCost;
                            adjC.hCost = hCost;
                            adjC.parent = cell;
                        }
                }
            }

            window.updateWindow();
            if (animationDelay != 0)
                Thread.sleep(animationDelay);
        }
    }

    private static Cell[] getAdjacentCells(Cell[][] grid, int x, int y) {
        ArrayList<Cell> adjacentCells = new ArrayList<>();

        if (x != grid[y].length - 1) {
            adjacentCells.add(grid[y][x + 1]); // Right
            if (y != 0)
                adjacentCells.add(grid[y-1][x+1]); // Top right
            if (y != grid.length - 1)
                adjacentCells.add(grid[y+1][x+1]); // Bottom right
        }

        if (y != 0)
            adjacentCells.add(grid[y-1][x]); // Top

        if (x != 0) {
            adjacentCells.add(grid[y][x - 1]); // Left
            if (y != 0)
                adjacentCells.add(grid[y-1][x-1]); // Top left
            if (y != grid.length - 1)
                adjacentCells.add(grid[y+1][x-1]); // Bottom left
        }

        if (y != grid.length - 1)
            adjacentCells.add(grid[y+1][x]); // Bottom

        Cell[] result = new Cell[adjacentCells.size()];
        return adjacentCells.toArray(result);
    }

}
