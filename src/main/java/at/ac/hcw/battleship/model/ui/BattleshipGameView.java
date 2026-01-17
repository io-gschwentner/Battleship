package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.logic.GameSetup;
import at.ac.hcw.battleship.model.GameBoard;

public class BattleshipGameView {
    private PlayerBoardView playerBoardView;
    private PlayerBoardView enemyBoardView;

    public BattleshipGameView(GameBoard playerGameBoard, GameSetup gameSetup) {
        this.playerBoardView = new PlayerBoardView(playerGameBoard);
        //this.enemyBoardView = new PlayerBoardView();
    }
}
