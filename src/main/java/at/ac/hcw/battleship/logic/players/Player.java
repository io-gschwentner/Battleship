package at.ac.hcw.battleship.logic.players;

import at.ac.hcw.battleship.model.Targetable;

/**
 * Common interface for all players (human or AI).
 */
public interface Player {

    /**
     * Performs one turn on the given targetable board.
     * For AI players this selects and fires a shot.
     * For a GUI human player this may be a no-op and the controller
     * triggers fireAt(...) when the user clicks.
     */
    void takeTurn(Targetable targetableGameBoard);
}
