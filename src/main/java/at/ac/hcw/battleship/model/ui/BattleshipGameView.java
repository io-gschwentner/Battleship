package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import javafx.scene.layout.HBox;

public class BattleshipGameView {
    private PlayerBoardView playerBoardView;
    private EnemyBoardView enemyBoardView;

    public BattleshipGameView(GameBoard playerGameBoard, GameBoard enemyGameBoard) {
        this.playerBoardView = new PlayerBoardView(playerGameBoard);
        this.enemyBoardView = new EnemyBoardView(enemyGameBoard);
    }

    public HBox createRoot(){
        HBox root = new HBox();
        root.getChildren().addAll(playerBoardView.createRoot(), enemyBoardView.createRoot());
        return root;
    }
}
