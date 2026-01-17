package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.CellState;
import at.ac.hcw.battleship.model.enums.KnownCellState;

public class KnownGameBoard {
    private final int size;
    private final KnownCellState[][] grid;


    public KnownGameBoard(int size) {
        this.size = size;
        this.grid = new KnownCellState[size][size];
    }

    public int getSize() {
        return size;
    }

    //get the cell state
    public KnownCellState getCell(int row, int col) {
        return grid[row][col];
    }

    public void setCell(int row, int col, KnownCellState state) { grid[row][col] = state; }
}
