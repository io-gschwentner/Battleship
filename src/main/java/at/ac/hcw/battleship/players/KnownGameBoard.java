package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.enums.KnownCellState;

/**
 * What a player/AI knows about the enemy board.
 * This does not know where ships really are, only shot results.
 */
public class KnownGameBoard {

    private final int size;
    private final KnownCellState[][] grid;

    public KnownGameBoard(int size) {
        this.size = size;
        this.grid = new KnownCellState[size][size];
        initUnknown();
    }

    private void initUnknown() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = KnownCellState.UNKNOWN;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public KnownCellState getCell(int row, int col) {
        return grid[row][col];
    }

    public void setCell(int row, int col, KnownCellState state) {
        grid[row][col] = state;
    }
}
