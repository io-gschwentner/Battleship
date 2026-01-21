package at.ac.hcw.battleship.logic.players;

import at.ac.hcw.battleship.model.Targetable;
import at.ac.hcw.battleship.model.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Very simple AI: picks random cells that have not been shot yet.
 */
public class EasyAiPlayer implements Player {

    private final Random random = new Random();
    private final List<Coordinates> remainingShots;

    public EasyAiPlayer(int boardSize) {
        this.remainingShots = new ArrayList<>();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                remainingShots.add(new Coordinates(row, col));
            }
        }
    }

    @Override
    public void takeTurn(Targetable targetableGameBoard) {
        if (remainingShots.isEmpty()) {
            return;
        }
        Coordinates shot = chooseShot();
        targetableGameBoard.fireAt(shot.row(), shot.col());
        System.out.println("AI shot at: " + shot.row() + "," + shot.col());
    }

    private Coordinates chooseShot() {
        int index = random.nextInt(remainingShots.size());
        Coordinates next = remainingShots.get(index);
        remainingShots.remove(index);
        return next;
    }
}
