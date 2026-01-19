package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.Targetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Very simple AI: picks random cells that have not been shot yet.
 */
public class EasyAiPlayer implements Player {

    private final Random random = new Random();
    private final List<Coord> remainingShots;

    public EasyAiPlayer(int boardSize) {
        this.remainingShots = new ArrayList<>();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                remainingShots.add(new Coord(row, col));
            }
        }
    }

    @Override
    public void takeTurn(Targetable targetableGameBoard) {
        if (remainingShots.isEmpty()) {
            return;
        }
        Coord shot = chooseShot();
        targetableGameBoard.fireAt(shot.row, shot.col);
        System.out.println("AI shot at: " + shot.row + "," + shot.col);
    }

    private Coord chooseShot() {
        int index = random.nextInt(remainingShots.size());
        Coord next = remainingShots.get(index);
        remainingShots.remove(index);
        return next;
    }
}
