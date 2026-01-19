package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.GameBoard;

/**
 * Evaluates the win/loss state of a battleship game
 * based on remaining ship cells on both boards.
 */
public class WinLossService {

    public enum GameResult {
        ONGOING,
        PLAYER1_WINS,
        PLAYER2_WINS,
        DRAW
    }

    /**
     * Compares remaining ship cells on both boards and returns the current game result.
     * A player wins when the opponent has no ship cells left.
     */
    public GameResult evaluate(GameBoard player1Board, GameBoard player2Board) {
        int p1Remaining = player1Board.getRemainingShipCells();
        int p2Remaining = player2Board.getRemainingShipCells();

        boolean p1Alive = p1Remaining > 0;
        boolean p2Alive = p2Remaining > 0;

        if (p1Alive && !p2Alive) {
            return GameResult.PLAYER1_WINS;
        }
        if (!p1Alive && p2Alive) {
            return GameResult.PLAYER2_WINS;
        }
        if (!p1Alive && !p2Alive) {
            return GameResult.DRAW;
        }
        return GameResult.ONGOING;
    }
}
