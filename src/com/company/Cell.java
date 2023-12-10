package com.company;

public class Cell {

    public static int getFcost(int gCost, int hCost) {
        return gCost + hCost;
    }

    public int x, y;
    public CellState state = CellState.NOT_VISITED;

    public int gCost;
    public int hCost;

    public Cell parent;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getFcost() {
        return getFcost(gCost, hCost);
    }

    @Override
    public String toString() {
        return "{x: " + x + ", y: " + y + ", State: " + state + ", F-cost: " + getFcost() + "}";
    }
}
