package at.ac.hcw.battleship.model.enums;

/**
 * What an AI or player knows about a cell on the enemy board.
 */
public enum KnownCellState {
    UNKNOWN, //not shot
    HIT, //shot and hit
    MISS, //shot and missed
    SUNK //shot and sunk ship
}
