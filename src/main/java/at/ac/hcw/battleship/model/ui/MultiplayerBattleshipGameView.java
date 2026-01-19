package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Main in-game view showing:
 *  - the player's own board,
 *  - the enemy board,
 *  - and a side panel for status / statistics.
 *
 * All game logic stays outside this class.
 */
public class MultiplayerBattleshipGameView implements BattleshipGameView {

    private final EnemyBoardView player1BoardView;
    private final EnemyBoardView player2BoardView;

    // we keep the actual nodes so we can disable them
    private BorderPane player1Root;
    private BorderPane player2Root;

    private final Label statusLabel;
    private final Label hitsLabel;
    private final Label missesLabel;
    private final Label enemyShipsLabel;

    private final Button backButton = new Button("Back to main menu");

    public MultiplayerBattleshipGameView(GameBoard playerGameBoard, GameBoard enemyGameBoard) {
        this.player1BoardView = new EnemyBoardView(playerGameBoard);
        this.player2BoardView = new EnemyBoardView(enemyGameBoard);

        this.statusLabel = new Label("Game started");
        this.statusLabel.getStyleClass().add("status-text");

        this.hitsLabel = new Label("Hits: 0");
        this.missesLabel = new Label("Misses: 0");
        this.enemyShipsLabel = new Label("Enemy ships remaining: 0");
    }

    /**
     * Builds the root layout:
     * [ player board | enemy board | stats panel ]
     */
    public HBox createRoot() {
        HBox root = new HBox(20);

        this.player1Root = player1BoardView.createRoot();
        this.player2Root = player2BoardView.createRoot();
        VBox statsPane = createStatsPane();

        root.getChildren().addAll(player1Root, player2Root, statsPane);
        root.setPadding(new Insets(10));
        return root;
    }

    private VBox createStatsPane() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.TOP_LEFT);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("stats-panel");

        Label title = new Label("Game Info");
        title.getStyleClass().add("stats-title");

        box.getChildren().addAll(title, statusLabel, hitsLabel, missesLabel, enemyShipsLabel, backButton);
        return box;
    }

    public void updateStats(int hits, int misses, int enemyShipsRemaining) {
        hitsLabel.setText("Hits: " + hits);
        missesLabel.setText("Misses: " + misses);
        enemyShipsLabel.setText("Enemy ships remaining: " + enemyShipsRemaining);
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    /** Enable / disable the board UI (used to enforce turn order). */
    public void swapDisabledBoard(){
        if (player1Root != null && player2Root != null) {
            player1Root.setDisable(!player1Root.isDisable());
            player2Root.setDisable(!player2Root.isDisable());
        }
    }

    public void setPlayer1BoardDisabled(boolean disabled) {
        if (player1Root != null) {
            player1Root.setDisable(disabled);
        }
    }
    public void setPlayer2BoardDisabled(boolean disabled) {
        if (player2Root != null) {
            player2Root.setDisable(disabled);
        }
    }

    public EnemyBoardView getPlayer1BoardView() {
        return player1BoardView;
    }

    public EnemyBoardView getPlayer2BoardView() {
        return player2BoardView;
    }

    public Button getBackButton() {
        return backButton;
    }
}
