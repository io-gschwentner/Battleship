package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.logic.GameSetup;
import at.ac.hcw.battleship.model.Ship;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * Interactive board used for ship placement for one player.
 */
public class BoardView extends InteractiveBoardView {

    private final GameSetup setup;
    private Ship currentShip;

    /**
     * Callback that is run when the user clicks "Start"
     * after all ships have been placed.
     */
    private Runnable onStartGame;

    public BoardView() {
        super();
        this.setup = new GameSetup();
    }

    public GameSetup getSetup() {
        return setup;
    }

    public void setOnStartGame(Runnable onStartGame) {
        this.onStartGame = onStartGame;
    }

    @Override
    protected void onCellClicked(int row, int col) {
        if (currentShip == null){
            currentShip = setup.getShips().get(shipIndex);
            statusLabel.setText("Place " + currentShip.toString());
       }

        int length = currentShip.getLength();
        boolean horizontal = currentShip.isHorizontal();

        boolean ok = board.placeShip(row, col, length, horizontal);
        if (!ok) {
            statusLabel.setText("Invalid placement");
            return;
        }

        currentShip.setPlaced(true);

//        next ship
        shipIndex++;
        currentShip = null;

        if (setup.allShipsPlaced()) {
            startGameButton.setDisable(false);
            statusLabel.setText("All ships have been placed!");
            return;
        }

        currentShip = setup.getShips().get(shipIndex);
        statusLabel.setText("Place " + currentShip.toString());
    }

    @Override
    protected HBox createBottomPanel() {
        HBox statusBar = new HBox(10);
        statusBar.getStyleClass().add("status-bar");
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(10));
        statusBar.setPrefHeight(40);

        currentShip = setup.getShips().get(shipIndex);
        statusLabel = new Label("Place " + currentShip.toString());
        statusLabel.getStyleClass().add("status-label");

        Button reset = new Button("Reset");
        reset.setPrefSize(80, 30);
        reset.setOnAction(e -> resetBoard());

        Button rotate = new Button("Rotate");
        rotate.setPrefSize(80, 30);
        rotate.setOnAction(e -> changeOrientation());

        startGameButton = new Button("Start");
        startGameButton.setPrefSize(60, 30);
        startGameButton.setDisable(true);
        startGameButton.setOnAction(e -> startGame());

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(
                leftSpacer,
                statusLabel,
                rotate,
                reset,
                startGameButton,
                rightSpacer
        );
        
        return statusBar;
    }

    private void resetBoard() {
        board.clearBoard();
        setup.reset();
        shipIndex = 0;
        currentShip = null;

        for (Ship ship : setup.getShips()) {
            ship.setPlaced(false);
            ship.setHorizontal(true);
        }

        startGameButton.setDisable(true);
        statusLabel.setText("Place " + currentShip.toString());
    }

    private void startGame() {

        if (onStartGame != null) {
            onStartGame.run();
        }
    }

    private void changeOrientation() {
        if (currentShip == null || currentShip.isPlaced()) {
            if (setup.allShipsPlaced()) {
                statusLabel.setText("All ships have been placed!");
                return;
            }
            if (shipIndex >= setup.getShips().size()) {
                statusLabel.setText("No more ships to place.");
                return;
            }
            currentShip = setup.getShips().get(shipIndex);
        }

        currentShip.setHorizontal(!currentShip.isHorizontal());
        statusLabel.setText("Place " + currentShip);
    }
}
