// Program.java
package at.ac.hcw.battleship;

import at.ac.hcw.battleship.logic.Game;
import at.ac.hcw.battleship.model.ui.BoardView;
import at.ac.hcw.battleship.model.GameBoard;
import javafx.application.Application;
import javafx.stage.Stage;

public class Program extends Application {

    @Override
    public void start(Stage stage) {
        // Player setup UI
        BoardView playerView = new BoardView(stage);
        GameBoard playerBoard = playerView.getBoard();

        // TODO: create enemy GameBoard and (later) enemy UI
        GameBoard enemyBoard = new GameBoard(BoardView.SIZE);

        // Game core (no UI here)
        Game game = new Game(playerBoard, enemyBoard);

        // TODO later:
        // - after all ships placed: start game loop or switch to a new scene
        // - use game.getWinLossService().evaluate(playerBoard, enemyBoard);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
