package at.ac.hcw.battleship;

import at.ac.hcw.battleship.logic.Game;
import at.ac.hcw.battleship.logic.WinLossService;
import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.GameMode;
import at.ac.hcw.battleship.model.ui.*;
import at.ac.hcw.battleship.players.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BattleshipApp extends Application {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 550;

    private GameMode gameMode = GameMode.EASY_AI;

    @Override
    public void start(Stage stage) {
        showSplash(stage);
    }

    // ---------- Splash ----------

    private void showSplash(Stage stage) {
        VBox root = createCenteredVBox(20,
                styledLabel("Battleship", 32),
                createButton("Start Game", () -> showGamemodeScene(stage))
        );
        setScene(stage, root, 600, 400, "Battleship");
    }

    // ---------- Menu ----------

    private void showGamemodeScene(Stage stage) {
        VBox root = createCenteredVBox(20,
                styledLabel("Battleship", 32),
                createButton("Easy", () -> startPlacement(stage, GameMode.EASY_AI)),
                createButton("Medium", () -> startPlacement(stage, GameMode.MEDIUM_AI)),
                createButton("vs Human", () -> startPlacement(stage, GameMode.TWO_PLAYERS))
        );
        setScene(stage, root, 600, 400, "Battleship");
    }

    private void startPlacement(Stage stage, GameMode mode) {
        this.gameMode = mode;

        if (mode == GameMode.TWO_PLAYERS) {
            continueMultiplayerPlacement(stage);
        } else {
            startSinglePlacement(stage);
        }
    }

    // ---------- Placement ----------

    private void startSinglePlacement(Stage stage) {
        BoardView setupView = new BoardView();
        BorderPane root = setupView.createRoot();

        setScene(stage, root, 1000, HEIGHT, "Place your ships");

        setupView.setOnStartGame(() ->
                startAiGame(stage, setupView.getBoard()));
    }

    private void continueMultiplayerPlacement(Stage stage) {
        BoardView p1View = new BoardView();
        setScene(stage, p1View.createRoot(), 1000, HEIGHT,
                "Player 1 – Place your ships");

        p1View.setOnStartGame(() ->
                continueMultiplayerPlacement(stage, p1View.getBoard()));
    }

    private void continueMultiplayerPlacement(Stage stage, GameBoard p1Board) {
        BoardView p2View = new BoardView();
        setScene(stage, p2View.createRoot(), 1000, HEIGHT,
                "Player 2 – Place your ships");

        p2View.setOnStartGame(() ->
                startMultiplayerGame(stage, p1Board, p2View.getBoard()));
    }

    // ---------- Game Start ----------

    private void startAiGame(Stage stage, GameBoard playerBoard) {
        GameBoard enemyBoard = new GameBoard(BoardView.SIZE);
        placeEnemyShips(enemyBoard);

        Game game = new Game(
                new HumanPlayer(),
                createOpponent(),
                playerBoard,
                enemyBoard
        );

        AiBattleshipGameView view =
                new AiBattleshipGameView(playerBoard, enemyBoard);

        setScene(stage, view.createRoot(), WIDTH, HEIGHT, "Battleship");

        setupAiHandlers(view, game, enemyBoard);
    }

    private void startMultiplayerGame(Stage stage,
                                      GameBoard p1Board,
                                      GameBoard p2Board) {

        Game game = new Game(
                new HumanPlayer(),
                new HumanPlayer(),
                p1Board,
                p2Board
        );

        MultiplayerBattleshipGameView view =
                new MultiplayerBattleshipGameView(p1Board, p2Board);

        setScene(stage, view.createRoot(), WIDTH, HEIGHT, "Battleship");

        setupMultiplayerHandlers(view, game, p1Board, p2Board);
    }

    // ---------- Turn Handling ----------

    private void setupAiHandlers(AiBattleshipGameView view,
                                 Game game,
                                 GameBoard enemyBoard) {

        int hits = 0;
        int misses = 0;

        view.updateStats(0, 0, enemyBoard.getRemainingShipCells());
        view.setStatus("Your turn");

        view.getEnemyBoardView().setOnHumanShot(coord -> {
            if (handleShot(coord, game, enemyBoard, hits, misses, view)) {
                view.setStatus("Enemy taking turn");
                game.playTurn(); // AI turn
                view.setEnemyBoardDisabled(false);
                view.setStatus("Your turn");
            }
        });
    }

    private void setupMultiplayerHandlers(MultiplayerBattleshipGameView view,
                                          Game game,
                                          GameBoard p1Board,
                                          GameBoard p2Board) {

        int hits = 0;
        int misses = 0;

        view.setPlayer1BoardDisabled(true);

        view.getPlayer2BoardView().setOnHumanShot(coord ->{
            view.swapDisabledBoard();
            handleShot(coord, game, p2Board, hits, misses, view);
        });

        view.getPlayer1BoardView().setOnHumanShot(coord -> {
            view.swapDisabledBoard();
            handleShot(coord, game, p1Board, hits, misses, view);
        });
    }

    private boolean handleShot(Coord coord,
                               Game game,
                               GameBoard enemyBoard,
                               int hits,
                               int misses,
                               BattleshipGameView view) {

        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            return false;
        }

        var result = enemyBoard.fireAt(coord.row, coord.col);

        switch (result) {
            case HIT, SUNK -> hits++;
            case MISS -> misses++;
        }

        view.updateStats(hits, misses,
                enemyBoard.getRemainingShipCells());

        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            return false;
        }

        game.playTurn();
        return true;
    }

    // ---------- Helpers ----------

    private Player createOpponent() {
        return switch (gameMode) {
            case TWO_PLAYERS -> new HumanPlayer();
            case MEDIUM_AI   -> new MediumAiPlayer(new KnownGameBoard(BoardView.SIZE));
            case EASY_AI     -> new EasyAiPlayer(BoardView.SIZE);
        };
    }

    private boolean isFinished(Game game) {
        return game.getResult() != WinLossService.GameResult.ONGOING;
    }

    private String gameResultText(Game game) {
        return switch (game.getResult()) {
            case PLAYER1_WINS -> "Game over – You win!";
            case PLAYER2_WINS -> "Game over – Opponent wins!";
            case DRAW         -> "Game over – Draw";
            default           -> "Game over";
        };
    }

    private void placeEnemyShips(GameBoard board) {
        board.placeShip(1, 1, 4, true);
        board.placeShip(3, 5, 4, false);
        board.placeShip(5, 2, 3, true);
        board.placeShip(7, 7, 3, false);
        board.placeShip(0, 8, 3, false);
        board.placeShip(8, 1, 2, true);
        board.placeShip(2, 9, 2, false);
    }

    private void setScene(Stage stage,
                          javafx.scene.Parent root,
                          int w,
                          int h,
                          String title) {
        Scene scene = new Scene(root, w, h);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createCenteredVBox(int spacing, javafx.scene.Node... nodes) {
        VBox box = new VBox(spacing, nodes);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Button createButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private Label styledLabel(String text, int size) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: " + size +
                "px; -fx-font-weight: bold;");
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
