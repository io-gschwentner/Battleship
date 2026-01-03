// logic/Game.java
package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.GameBoard;

public class Game {
    private final GameBoard playerBoard;
    private final GameBoard enemyBoard;
    private final WinLossService winLossService;

    public Game(GameBoard playerBoard, GameBoard enemyBoard) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.winLossService = new WinLossService();
    }

    public void start() {
        // later: game loop, calls to applyShot(), win/loss check, opponent moves
    }

    public GameBoard getPlayerBoard() {
        return playerBoard;
    }

    public GameBoard getEnemyBoard() {
        return enemyBoard;
    }

    public WinLossService getWinLossService() {
        return winLossService;
    }
}
