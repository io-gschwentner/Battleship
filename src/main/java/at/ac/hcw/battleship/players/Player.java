package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.Targetable;

public interface Player {
    void takeTurn(Targetable targetableGameBoard);
}
