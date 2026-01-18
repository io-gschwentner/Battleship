package at.ac.hcw.battleship.model;

import at.ac.hcw.battleship.model.enums.CellState;

public interface Targetable {
    CellState fireAt(int row, int col);
}
