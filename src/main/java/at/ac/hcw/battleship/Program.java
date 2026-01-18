// Program.java
package at.ac.hcw.battleship;

import at.ac.hcw.battleship.logic.Game;
import at.ac.hcw.battleship.logic.GameSetup;
import at.ac.hcw.battleship.model.ui.BoardView;
import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.ui.PlayerBoardView;
import at.ac.hcw.battleship.players.EasyAiPlayer;
import at.ac.hcw.battleship.players.HumanPlayer;
import at.ac.hcw.battleship.players.KnownGameBoard;
import at.ac.hcw.battleship.players.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Program extends Application {

    @Override
    public void start(Stage stage) {
        // Player setup UI
//        GameBoard test = new GameBoard(10);
//        test.placeShip(2,3,4,true);
//        PlayerBoardView playerView = new PlayerBoardView(test);

        BoardView playerView = new BoardView();

        BorderPane root = playerView.createRoot();
        Scene scene = new Scene(root, 500, 550);
        stage.setTitle("Place your ships");

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.show();


        GameBoard playerBoard = playerView.getBoard();

        // TODO: create enemy GameBoard and (later) enemy UI
        GameBoard enemyBoard = new GameBoard(BoardView.SIZE);

        // Game core (no UI here)
        //Player humanPlayer = new HumanPlayer();
        //Player easyAiPlayer = new EasyAiPlayer(new KnownGameBoard(BoardView.SIZE));
        //Game game = new Game(humanPlayer, easyAiPlayer, playerBoard, enemyBoard);

        // TODO later:
        // - after all ships placed: start game loop or switch to a new scene
        // - use game.getWinLossService().evaluate(playerBoard, enemyBoard);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
