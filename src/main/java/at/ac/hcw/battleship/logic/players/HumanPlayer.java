package at.ac.hcw.battleship.logic.players;

import at.ac.hcw.battleship.model.Targetable;

/**
 * Human player in a JavaFX UI.
 * The actual shot is triggered by mouse clicks in the UI,
 * so this implementation does nothing in takeTurn().
 */
public class HumanPlayer implements Player {

    @Override
    public void takeTurn(Targetable targetableGameBoard) {
        // For a GUI-based game, the controller calls fireAt(...)
        // when the user clicks a cell on the enemy board.
    }
}
