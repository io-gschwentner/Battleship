package at.ac.hcw.battleship;

import at.ac.hcw.battleship.logic.Game;
import at.ac.hcw.battleship.logic.GameSetup;
import at.ac.hcw.battleship.logic.WinLossService;
import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.Ship;
import at.ac.hcw.battleship.model.Stats;
import at.ac.hcw.battleship.model.enums.GameMode;
import at.ac.hcw.battleship.model.ui.*;
import at.ac.hcw.battleship.players.*;
import at.ac.hcw.battleship.logic.RandomShipPlacement;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

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

        view.getBackButton().setOnAction(e -> showGamemodeScene(stage));

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

        view.getBackButton().setOnAction(e -> showGamemodeScene(stage));

        setupMultiplayerHandlers(view, game, p1Board, p2Board);
    }

    // ---------- Turn Handling ----------

    private void setupAiHandlers(AiBattleshipGameView view,
                                 Game game,
                                 GameBoard enemyBoard) {

        Stats stats = new Stats(); // hits/misses for the human vs AI

        view.updateStats(0, 0, enemyBoard.getRemainingShipCells());
        view.setStatus("Your turn");

        view.getEnemyBoardView().setOnHumanShot(coord -> {
            if (handleShot(coord, game, enemyBoard, stats, view)) {
                view.updateStats(stats.hits, stats.misses, enemyBoard.getRemainingShipCells());
                view.setStatus("Enemy taking turn");
                if (isFinished(game)) {
                    view.setStatus(gameResultText(game));
                    view.disableInteractions();
                    return;
                }
                game.playTurn(); //pass turn to human
                view.setEnemyBoardDisabled(false);
                view.setStatus("Your turn");
            }
        });
    }

    private void setupMultiplayerHandlers(MultiplayerBattleshipGameView view,
                                          Game game,
                                          GameBoard p1Board,
                                          GameBoard p2Board) {

        Stats p1Stats = new Stats(); // stats for player 1 shooting at p2Board
        Stats p2Stats = new Stats(); // stats for player 2 shooting at p1Board

        view.setPlayer1BoardDisabled(true);

        // Player 1 shoots at Player 2's board
        view.getPlayer2BoardView().setOnHumanShot(coord -> {
            view.swapDisabledBoard();
            handleShot(coord, game, p2Board, p1Stats, view);
            view.updateStats(p2Stats.hits, p2Stats.misses, p1Board.getRemainingShipCells());
        });

        // Player 2 shoots at Player 1's board
        view.getPlayer1BoardView().setOnHumanShot(coord -> {
            view.swapDisabledBoard();
            handleShot(coord, game, p1Board, p2Stats, view);
            view.updateStats(p1Stats.hits, p1Stats.misses, p2Board.getRemainingShipCells());
        });
    }

    private boolean handleShot(Coord coord,
                               Game game,
                               GameBoard enemyBoard,
                               Stats stats,
                               BattleshipGameView view) {

        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            view.disableInteractions();
            return false;
        }

        var result = enemyBoard.fireAt(coord.row, coord.col);

        switch (result) {
            case HIT, SUNK -> stats.hits++;
            case MISS      -> stats.misses++;
        }

        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            view.disableInteractions();
            return false;
        }

        game.playTurn(); //pass turn (to AI/ other Player)
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

    /**
     * Generated placement for AI ships (same fleet size as player).
     */
    private void placeEnemyShips(GameBoard enemyBoard) {

        GameSetup gameSetup = new GameSetup();
        List<Integer> shipLengths = gameSetup.getShips()
                .stream()
                .map(Ship::getLength)
                .toList();
        RandomShipPlacement.placeRandomShips(enemyBoard, shipLengths);
    }

    private boolean isFinished(Game game) {
        return game.getResult() != WinLossService.GameResult.ONGOING;
    }

    private String gameResultText(Game game) {
        if(gameMode == GameMode.TWO_PLAYERS){
            return switch (game.getResult()) {
                case PLAYER1_WINS -> "Game over – Player 1 wins!";
                case PLAYER2_WINS -> "Game over – Player 2 wins!";
                case DRAW         -> "Game over – Draw";
                default           -> "Game over";
            };
        }
        return switch (game.getResult()) {
            case PLAYER1_WINS -> "Game over – You win!";
            case PLAYER2_WINS -> "Game over – Opponent wins!";
            case DRAW         -> "Game over – Draw";
            default           -> "Game over";
        };
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
