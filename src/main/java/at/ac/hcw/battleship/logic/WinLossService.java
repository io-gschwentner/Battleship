// logic/WinLossService.java
package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.GameBoard;

public class WinLossService {

    public enum GameResult {
        ONGOING, PLAYER1_WINS, PLAYER2_WINS, DRAW
    }

    public GameResult evaluate(GameBoard playerBoard, GameBoard enemyBoard) {
        int p1Remaining = playerBoard.getRemainingShipCells();
        int p2Remaining = enemyBoard.getRemainingShipCells();

        boolean p1Alive = p1Remaining > 0;
        boolean p2Alive = p2Remaining > 0;

        if (p1Alive && !p2Alive) return GameResult.PLAYER1_WINS;
        if (!p1Alive && p2Alive) return GameResult.PLAYER2_WINS;
        if (!p1Alive && !p2Alive) return GameResult.DRAW;
        return GameResult.ONGOING;
    }
}
