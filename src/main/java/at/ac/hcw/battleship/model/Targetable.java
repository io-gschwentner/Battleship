package at.ac.hcw.battleship.model;

import at.ac.hcw.battleship.model.enums.CellState;

/**
 * Represents something that can be targeted with a shot
 * (e.g. a GameBoard).
 */
public interface Targetable {

    /**
     * Fires at the given cell and returns the resulting cell state
     * (HIT, MISS, SUNK, or the old state if already shot).
     */
    CellState fireAt(int row, int col);
}
