package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.CellState;
import at.ac.hcw.battleship.model.enums.KnownCellState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasyAiPlayer implements Player{
    private final KnownGameBoard gameState;
    private final Random random = new Random(System.currentTimeMillis());
    private final List<Coord> notShot;

    public EasyAiPlayer(KnownGameBoard gameState) {
        this.gameState = gameState;

        notShot = new ArrayList<>();
        for(int i = 0; i < gameState.getSize(); i++){
            for(int j = 0; j < gameState.getSize(); j++){
                notShot.add(new Coord(i, j));
            }
        }
    }

    @Override
    public void takeTurn(GameBoard enemyBoard) { //checkerboard and sink strat
        Coord shot = chooseShot();

        CellState result = enemyBoard.fireAt(shot.row, shot.col);

        processResult(shot, result);
    }

    private Coord chooseShot() {
        Coord nextShot = notShot.get(random.nextInt(notShot.size()));
        notShot.remove(nextShot);
        return nextShot;
    }

    private void processResult(Coord shot, CellState result) {
        if (result == CellState.HIT) {
            gameState.setCell(shot.row, shot.col, KnownCellState.HIT);
        } else if (result == CellState.MISS) {
            gameState.setCell(shot.row, shot.col, KnownCellState.MISS);
        }
    }
}
