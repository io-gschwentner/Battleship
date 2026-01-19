package at.ac.hcw.battleship.model.enums;

/**
 * Internal state of a cell on a GameBoard.
 */
public enum CellState {
    EMPTY,   // no ship, not shot
    SHIP,    // part of a ship, not hit
    HIT,     // ship cell that has been hit
    MISS,    // shot, but no ship
    SUNK     // ship cell that belongs to a sunk ship
}
