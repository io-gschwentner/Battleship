package at.ac.hcw.battleship.model.enums;

/**
 * What an AI or player knows about a cell on the enemy board.
 */
public enum KnownCellState {
    UNKNOWN,
    HIT,
    MISS,
    SUNK
}
