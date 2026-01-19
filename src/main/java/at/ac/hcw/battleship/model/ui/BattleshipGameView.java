package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
public class BattleshipGameView {

    private final PlayerBoardView playerBoardView;
    private final EnemyBoardView enemyBoardView;

    // we keep the actual node so we can disable it
    private BorderPane enemyRoot;

    private final Label statusLabel;
    private final Label hitsLabel;
    private final Label missesLabel;
    private final Label enemyShipsLabel;

    public BattleshipGameView(GameBoard playerGameBoard, GameBoard enemyGameBoard) {
        this.playerBoardView = new PlayerBoardView(playerGameBoard);
        this.enemyBoardView = new EnemyBoardView(enemyGameBoard);

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

        BorderPane playerPane = playerBoardView.createRoot();
        this.enemyRoot = enemyBoardView.createRoot();
        VBox statsPane = createStatsPane();

        root.getChildren().addAll(playerPane, enemyRoot, statsPane);
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

        box.getChildren().addAll(title, statusLabel, hitsLabel, missesLabel, enemyShipsLabel);
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

    /** Enable / disable the enemy board UI (used to enforce turn order). */
    public void setEnemyBoardDisabled(boolean disabled) {
        if (enemyRoot != null) {
            enemyRoot.setDisable(disabled);
        }
    }

    public PlayerBoardView getPlayerBoardView() {
        return playerBoardView;
    }

    public EnemyBoardView getEnemyBoardView() {
        return enemyBoardView;
    }
}
