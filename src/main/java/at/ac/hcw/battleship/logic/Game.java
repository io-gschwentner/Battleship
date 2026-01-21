package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.logic.players.Player;

/**
 * Coordinates a Battleship match between two players on two boards.
 * In this project, the human move is triggered by the UI (mouse click),
 * and playTurn() is typically used for the opponent (AI or second human).
 */
public class Game {

    private final Player player1;
    private final GameBoard player1Board;
    private final Player player2;
    private final GameBoard player2Board;
    private final WinLossService winLossService;

    /**
     * The player whose turn it is when playTurn() is called.
     */
    private Player activePlayer;

    public Game(Player player1,
                Player player2,
                GameBoard player1Board,
                GameBoard player2Board) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        this.winLossService = new WinLossService();

        // Human moves via UI; playTurn() should execute the opponent's move.
        this.activePlayer = player2;
    }

    /**
     * Executes exactly one turn for the active player and then switches turns.
     * The UI should call getResult() afterwards to check if the game has ended.
     */
    public void playTurn() {
        if (isFinished()) {
            return;
        }
        if (activePlayer == player1) {
            activePlayer.takeTurn(player2Board);
            activePlayer = player2;
        } else {
            activePlayer.takeTurn(player1Board);
            activePlayer = player1;
        }
    }

    /**
     * Returns the current game result (ONGOING, PLAYER1_WINS, PLAYER2_WINS, DRAW).
     */
    public WinLossService.GameResult getResult() {
        return winLossService.evaluate(player1Board, player2Board);
    }

    /**
     * Convenience method to check if the game has already finished.
     */
    public boolean isFinished() {
        return getResult() != WinLossService.GameResult.ONGOING;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public GameBoard getPlayer1Board() {
        return player1Board;
    }

    public GameBoard getPlayer2Board() {
        return player2Board;
    }

    public WinLossService getWinLossService() {
        return winLossService;
    }
}
