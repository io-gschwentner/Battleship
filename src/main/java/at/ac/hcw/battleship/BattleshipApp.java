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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BattleshipApp extends Application {

    // default: Easy AI
    private GameMode gameMode = GameMode.EASY_AI;

    @Override
    public void start(Stage stage) {
        showSplash(stage);
    }

    // ---------- Splash screen ----------

    private void showSplash(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Battleship");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> showGamemodeScene(stage));

        root.getChildren().addAll(title, startButton);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Battleship");
        stage.setScene(scene);
        stage.show();
    }

    // ---------- Gamemode picker ----------

    private void showGamemodeScene(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Battleship");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        Button vsEasyAiButton = new Button("Easy");
        vsEasyAiButton.setOnAction(e -> {gameMode = GameMode.EASY_AI; showPlacementScene(stage);});

        Button vsMediumAiButton = new Button("Medium");
        vsMediumAiButton.setOnAction(e -> {gameMode = GameMode.MEDIUM_AI; showPlacementScene(stage);});

        Button vsHumanButton = new Button("vs Human");
        vsHumanButton.setOnAction(e -> {gameMode = GameMode.TWO_PLAYERS; showPlacementSceneMultiplayer(stage);});

        root.getChildren().addAll(title, vsEasyAiButton, vsMediumAiButton, vsHumanButton);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Battleship");
        stage.setScene(scene);
        stage.show();
    }

    // ---------- Placement scene ----------

    private void showPlacementScene(Stage stage) {
        BoardView setupView = new BoardView();
        BorderPane root = setupView.createRoot();

        Scene scene = new Scene(root, 1000, 550);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Battleship – Place your ships");
        stage.setScene(scene);
        stage.show();

        setupView.setOnStartGame(() ->
                startGameScene(stage, setupView.getBoard()));
    }

    // ---------- Placement scene multiplayer ----------

    private void showPlacementSceneMultiplayer(Stage stage) {
        BoardView setupViewPlayer1 = new BoardView();
        BorderPane root = setupViewPlayer1.createRoot();

        Scene scene = new Scene(root, 1000, 550);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Battleship – Player 1: Place your ships");
        stage.setScene(scene);
        stage.show();

        setupViewPlayer1.setOnStartGame(() ->
                showPlacementSceneMultiplayer(stage, setupViewPlayer1.getBoard()));
    }
    private void showPlacementSceneMultiplayer(Stage stage, GameBoard gameBoardPlayer1) {
        BoardView setupViewPlayer2 = new BoardView();
        BorderPane root = setupViewPlayer2.createRoot();

        Scene scene = new Scene(root, 1000, 550);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Battleship – Player 2: Place your ships");
        stage.setScene(scene);
        stage.show();

        setupViewPlayer2.setOnStartGame(() ->
                startGameScene(stage, gameBoardPlayer1, setupViewPlayer2.getBoard()));
    }

    // ---------- Game scene ----------
    private void startGameScene(Stage stage, GameBoard player1Board, GameBoard player2Board) {
        Player humanPlayer = new HumanPlayer();
        Player opponent = new HumanPlayer();

        Game game = new Game(humanPlayer, opponent, player1Board, player2Board);

        MultiplayerBattleshipGameView gameView = new MultiplayerBattleshipGameView(player1Board, player2Board);
        HBox root = gameView.createRoot();

        Scene scene = new Scene(root, 1200, 550);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Battleship – Game");
        stage.setScene(scene);
        stage.show();

        final int[] hits = {0};
        final int[] misses = {0};

        gameView.updateStats(hits[0], misses[0], player2Board.getRemainingShipCells());
        gameView.setStatus("Player 1 turn");

        EnemyBoardView player2BoardView = gameView.getPlayer2BoardView();
        gameView.setPlayer2BoardDisabled(false);

        player2BoardView.setOnHumanShot(coord -> {
            System.out.println("Enemy cell clicked: " + coord.row + "," + coord.col);
            handleShot(coord, game, player2Board, hits, misses, gameView);
        });

        EnemyBoardView player1BoardView = gameView.getPlayer1BoardView();
        gameView.setPlayer1BoardDisabled(true);

        player1BoardView.setOnHumanShot(coord -> {
            System.out.println("Enemy cell clicked: " + coord.row + "," + coord.col);
            handleShot(coord, game, player1Board, hits, misses, gameView);
        });
    }

    private void handleShot(Coord coord,
                                 Game game,
                                 GameBoard enemyBoard,
                                 int[] hits,
                                 int[] misses,
                                 MultiplayerBattleshipGameView view) {
        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            return;
        }

        // Disable enemy board so the player cannot click twice
        view.swapBoardDisable();

        var result = enemyBoard.fireAt(coord.row, coord.col);

        switch (result) {
            case HIT, SUNK -> hits[0]++;
            case MISS -> misses[0]++;
            default -> { }
        }

        view.updateStats(hits[0], misses[0], enemyBoard.getRemainingShipCells());

        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            return;
        }

        // Next Player turn
        game.playTurn();
    }

    private void startGameScene(Stage stage, GameBoard player1Board) {
        GameBoard player2Board = new GameBoard(BoardView.SIZE);
        placeEnemyShips(player2Board);

        Player humanPlayer = new HumanPlayer();
        Player opponent = createOpponent();

        Game game = new Game(humanPlayer, opponent, player1Board, player2Board);

        AiBattleshipGameView gameView = new AiBattleshipGameView(player1Board, player2Board);
        HBox root = gameView.createRoot();

        Scene scene = new Scene(root, 1200, 550);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Battleship – Game");
        stage.setScene(scene);
        stage.show();

        final int[] hits = {0};
        final int[] misses = {0};

        gameView.updateStats(hits[0], misses[0], player2Board.getRemainingShipCells());
        gameView.setStatus("Your turn");

        EnemyBoardView enemyView = gameView.getEnemyBoardView();
        gameView.setEnemyBoardDisabled(false);

        enemyView.setOnHumanShot(coord -> {
            System.out.println("Enemy cell clicked: " + coord.row + "," + coord.col);
            handleHumanShot(coord, game, player2Board, hits, misses, gameView);
        });
    }

    private void handleHumanShot(Coord coord,
                                 Game game,
                                 GameBoard enemyBoard,
                                 int[] hits,
                                 int[] misses,
                                 AiBattleshipGameView view) {
        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            return;
        }

        // Disable enemy board so the player cannot click twice
        view.setEnemyBoardDisabled(true);

        var result = enemyBoard.fireAt(coord.row, coord.col);

        switch (result) {
            case HIT, SUNK -> hits[0]++;
            case MISS -> misses[0]++;
            default -> { }
        }

        view.updateStats(hits[0], misses[0], enemyBoard.getRemainingShipCells());

        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
            return;
        }

        // AI turn
        game.playTurn();

        if (isFinished(game)) {
            view.setStatus(gameResultText(game));
        } else {
            view.setStatus("Your turn");
            // Re-enable enemy board for the next human shot
            view.setEnemyBoardDisabled(false);
            // logic pass turn to human
            game.playTurn();
        }
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
     * Simple fixed placement for AI ships (same fleet size as player).
     * Can be improved to random placement later.
     */
    private void placeEnemyShips(GameBoard enemyBoard) {
        enemyBoard.placeShip(1, 1, 4, true);
        enemyBoard.placeShip(3, 5, 4, false);
        enemyBoard.placeShip(5, 2, 3, true);
        enemyBoard.placeShip(7, 7, 3, false);
        enemyBoard.placeShip(0, 8, 3, false);
        enemyBoard.placeShip(8, 1, 2, true);
        enemyBoard.placeShip(2, 9, 2, false);
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

    public static void main(String[] args) {
        launch(args);
    }
}
