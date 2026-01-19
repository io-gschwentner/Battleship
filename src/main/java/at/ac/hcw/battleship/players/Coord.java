package at.ac.hcw.battleship.players;

/**
 * Immutable row/column coordinate on a GameBoard.
 */
public class Coord {
    public final int row;
    public final int col;

    public Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
