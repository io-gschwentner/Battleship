package at.ac.hcw.battleship.model;


import at.ac.hcw.battleship.model.enums.CellState;

public class GameBoard {
    private final int size;
    private final CellState[][] grid;


    public GameBoard(int size) {
        this.size = size;
        this.grid = new CellState[size][size];
        clearBoard();
    }

    public int getSize() {
        return size;
    }

    //get the cell state
    public CellState getCell(int row, int col) {
        return grid[row][col];
    }

    //set every cell to EMPTY
    public void clearBoard() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = CellState.EMPTY;
            }
        }
    }

    //check if rows and cols are in bounds (0 and size-1)
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    //placing ships (only multi-tile ships)
    public boolean placeShip(int row, int col, int length, boolean horizontal) {
        int endRow = horizontal ? row : row + length - 1;    //find end row of the ship
        int endCol = horizontal ? col + length - 1 : col;   //find end col of the ship

        //cell validation (BOUNDS)
        if (!inBounds(row, col) || !inBounds(endRow, endCol)) {
            return false;
        }
        //validation (EMPTY?)
        for (int r = row; r <= endRow; r++) {
            for (int c = col; c <= endCol; c++) {
                if (grid[r][c] != CellState.EMPTY) {
                    return false;
                }
            }
        }

        //place ship
        for (int r = row; r <= endRow; r++) {
            for (int c = col; c <= endCol; c++) {
                grid[r][c] = CellState.SHIP;
            }
        }
        return true;
    }
    public int getRemainingShipCells() {
        int count = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c] == CellState.SHIP) {
                    count++;
                }
            }
        }
        return count;
    }

    public CellState fireAt(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IllegalArgumentException("Shot out of bounds");
        }

        if (grid[row][col] == CellState.SHIP) {
            grid[row][col] = CellState.HIT;
            return CellState.HIT;
        }

        if (grid[row][col] == CellState.EMPTY) {
            grid[row][col] = CellState.MISS;
            return CellState.MISS;
        }

        // already shot here
        return grid[row][col];
    }
}
