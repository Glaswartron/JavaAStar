package com.company;

public class Grid {

    public Cell[][] cells;

    public int width, height;
    public int cellSideLength;

    public Grid(int width, int height, int cellSideLength) {
        this.width = width;
        this.height = height;
        this.cellSideLength = cellSideLength;

        cells = new Cell[height][width];

        // Initialize the cells
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[y][x] = new Cell(x, y);
            }
        }
    }
}
