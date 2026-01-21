package at.ac.hcw.battleship.model;

import at.ac.hcw.battleship.model.enums.CellState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a square Battleship board with ships and shots.
 * Uses JavaFX properties so the UI can bind to cell changes.
 */
public class GameBoard implements Targetable {

    private final int size;
    private final ObjectProperty<CellState>[][] grid;
    private final List<Ship> ships;

    @SuppressWarnings("unchecked")
    public GameBoard(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be positive");
        }
        this.size = size;
        this.grid = new ObjectProperty[size][size];
        this.ships = new ArrayList<>();
        initializeBoard();
    }

    public int getSize() {
        return size;
    }

    /**
     * Property access for JavaFX binding.
     */
    public ObjectProperty<CellState> cellProperty(int row, int col) {
        checkBounds(row, col);
        return grid[row][col];
    }

    /**
     * Returns the current cell state at (row, col).
     */
    public CellState getCell(int row, int col) {
        checkBounds(row, col);
        return grid[row][col].get();
    }

    /**
     * Clears the board to EMPTY and removes all ships.
     */
    public void clearBoard() {
        ships.clear();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c].set(CellState.EMPTY);
            }
        }
    }

    private void initializeBoard(){
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = new SimpleObjectProperty<>(CellState.EMPTY);
            }
        }
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private void checkBounds(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException(
                    "Cell out of bounds: (" + row + "," + col + ")");
        }
    }

    /**
     * Places a new ship of given length starting at (row, col).
     * Returns true if placement is valid and successful.
     */
    public boolean placeShip(int row, int col, int length, boolean horizontal) {
        if (length <= 0) {
            return false;
        }

        int endRow = horizontal ? row : row + length - 1;
        int endCol = horizontal ? col + length - 1 : col;

        // bounds check
        if (!inBounds(row, col) || !inBounds(endRow, endCol)) {
            return false;
        }

        // no overlap
        for (int r = row; r <= endRow; r++) {
            for (int c = col; c <= endCol; c++) {
                if (grid[r][c].get() != CellState.EMPTY) {
                    return false;
                }
            }
        }

        // place ship and record its coordinates
        Ship ship = new Ship("Ship", length);
        for (int r = row; r <= endRow; r++) {
            for (int c = col; c <= endCol; c++) {
                grid[r][c].set(CellState.SHIP);
                ship.addCoordinate(new Coordinates(r, c));
            }
        }
        ships.add(ship);
        return true;
    }

    /**
     * Counts how many ship cells are still not hit/sunk.
     * This is used by WinLossService for win/loss evaluation.
     */
    public int getRemainingShipCells() {
        int count = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c].get() == CellState.SHIP) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Finds the ship occupying the given cell, or null if none.
     */
    private Ship findShipAt(int row, int col) {
        for (Ship ship : ships) {
            for (Coordinates coordinate : ship.getCoordinates()) {
                if (coordinate.row() == row && coordinate.col() == col) {
                    return ship;
                }
            }
        }
        return null;
    }

    /**
     * Fires at (row, col) and updates the board:
     * - SHIP  -> HIT or SUNK (and marks all cells of that ship as SUNK)
     * - EMPTY -> MISS
     * - already HIT/MISS/SUNK -> unchanged
     */
    @Override
    public CellState fireAt(int row, int col) {
        checkBounds(row, col);

        CellState current = grid[row][col].get();

        if (current == CellState.SHIP) {
            Ship ship = findShipAt(row, col);
            if (ship == null) {
                throw new IllegalStateException("No ship found for hit cell at (" + row + "," + col + ")");
            }

            grid[row][col].set(CellState.HIT);
            ship.addHit();

            if (ship.isSunk()) {
                for (Coordinates coordinate : ship.getCoordinates()) {
                    grid[coordinate.row()][coordinate.col()].set(CellState.SUNK);
                }
                return CellState.SUNK;
            }
            return CellState.HIT;
        }

        if (current == CellState.EMPTY) {
            grid[row][col].set(CellState.MISS);
            return CellState.MISS;
        }

        // already shot here (HIT, MISS or SUNK)
        return current;
    }
}
