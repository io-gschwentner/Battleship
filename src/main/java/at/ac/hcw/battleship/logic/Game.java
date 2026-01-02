package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.Board;

public class Game {
    private final Board playerBoard;
    private final Board enemyBoard;
    private final WinLossService winLossService;

    public Game(Board playerBoard, Board enemyBoard) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.winLossService = new WinLossService();
    }

    public void start() {
        // loop over turns, ask UI for moves, call applyShot, then:
        // WinLossService.GameResult result = winLossService.evaluate(playerBoard, enemyBoard);
        // if result != ONGOING -> show message and break.
    }
}
