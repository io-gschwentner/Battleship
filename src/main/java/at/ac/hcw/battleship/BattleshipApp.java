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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class BattleshipApp extends Application {

    private static final int MENU_WIDTH = 600;
    private static final int MENU_HEIGHT = 400;
    private static final int PLACEMENT_WIDTH = 1000;
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 550;
    private static final int SPACING = 20;
    private static final int FONT_SIZE = 32;
    private static final String TITLE = "Battleship";

    private GameMode gameMode = GameMode.EASY_AI;

    @Override
    public void start(Stage stage) {
        showSplash(stage);
    }

    // ---------- Splash ----------

    private void showSplash(Stage stage) {
        VBox root = createCenteredVBox(
                styledLabel(),
                createButton("Start Game", () -> showGameModeScene(stage))
        );
        setScene(stage, root, MENU_WIDTH, MENU_HEIGHT, TITLE);
    }

    // ---------- Menu ----------

    private void showGameModeScene(Stage stage) {
        VBox root = createCenteredVBox(
                styledLabel(),
                createButton("Easy", () -> startPlacement(stage, GameMode.EASY_AI)),
                createButton("Medium", () -> startPlacement(stage, GameMode.MEDIUM_AI)),
                createButton("vs Human", () -> startPlacement(stage, GameMode.TWO_PLAYERS))
        );
        setScene(stage, root, MENU_WIDTH, MENU_HEIGHT, TITLE);
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

        setScene(stage, root, PLACEMENT_WIDTH, HEIGHT, "Place your ships");

        setupView.setOnStartGame(() ->
                startAiGame(stage, setupView.getBoard()));
    }

    private void continueMultiplayerPlacement(Stage stage) {
        BoardView p1View = new BoardView();
        setScene(stage, p1View.createRoot(), PLACEMENT_WIDTH, HEIGHT,
                "Player 1 – Place your ships");

        p1View.setOnStartGame(() ->
                continueMultiplayerPlacement(stage, p1View.getBoard()));
    }

    private void continueMultiplayerPlacement(Stage stage, GameBoard p1Board) {
        BoardView p2View = new BoardView();
        setScene(stage, p2View.createRoot(), PLACEMENT_WIDTH, HEIGHT,
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

        setScene(stage, view.createRoot(), WIDTH, HEIGHT, TITLE);

        view.getBackButton().setOnAction(e -> showGameModeScene(stage));

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

        setScene(stage, view.createRoot(), WIDTH, HEIGHT, TITLE);

        view.getBackButton().setOnAction(e -> showGameModeScene(stage));

        setupMultiplayerHandlers(view, game, p1Board, p2Board);
    }

    // ---------- Turn Handling ----------

    private void setupAiHandlers(AiBattleshipGameView view,
                                 Game game,
                                 GameBoard enemyBoard) {

        Stats stats = new Stats(); // hits/misses for the human vs AI

        view.updateStats(stats.hits, stats.misses, enemyBoard.getRemainingShipCells());
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

    private boolean handleShot(Coordinates coordinates,
                               Game game,
                               GameBoard enemyBoard,
                               Stats stats,
                               BattleshipGameView view) {

        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            view.disableInteractions();
            return false;
        }

        var result = enemyBoard.fireAt(coordinates.row(), coordinates.col());

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
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createCenteredVBox(Node... nodes) {
        VBox box = new VBox(BattleshipApp.SPACING, nodes);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Button createButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private Label styledLabel() {
        Label label = new Label(TITLE);
        label.setStyle("-fx-text-fill: white; -fx-font-size: " + FONT_SIZE +
                "px; -fx-font-weight: bold;");
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
