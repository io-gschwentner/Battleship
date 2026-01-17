package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.GameBoard;

public interface Player {
    void takeTurn(GameBoard enemyBoard);
}
