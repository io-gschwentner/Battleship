package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.Targetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasyAiPlayer implements Player{
    private final Random random = new Random(System.currentTimeMillis());
    private final List<Coord> notShot;

    public EasyAiPlayer(KnownGameBoard gameState) {
        notShot = new ArrayList<>();
        for(int i = 0; i < gameState.getSize(); i++){
            for(int j = 0; j < gameState.getSize(); j++){
                notShot.add(new Coord(i, j));
            }
        }
    }

    @Override
    public void takeTurn(Targetable targetableGameBoard) {
        Coord shot = chooseShot();
        targetableGameBoard.fireAt(shot.row, shot.col);
    }

    private Coord chooseShot() {
        Coord nextShot = notShot.get(random.nextInt(notShot.size()));
        notShot.remove(nextShot);
        return nextShot;
    }
}
