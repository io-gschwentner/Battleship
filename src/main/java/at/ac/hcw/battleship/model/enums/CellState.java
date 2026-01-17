package at.ac.hcw.battleship.model.enums;

// enums for defining the cell state
public enum CellState {
    EMPTY,          //no ship, not shot
    SHIP,           //ship, not hit
    HIT,            //ship & hit
    MISS,           //shot, no ship
    SUNK,           //ship, hit & sunk
}
