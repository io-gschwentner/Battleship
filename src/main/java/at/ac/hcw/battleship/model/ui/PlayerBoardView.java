package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/**
 * Read‑only view of the player's own board during the game.
 */
public class PlayerBoardView extends InteractiveBoardView {

    public PlayerBoardView(GameBoard gameBoard) {
        super(gameBoard);
    }

    @Override
    protected void onCellClicked(int row, int col) {
        // Do nothing; player board is read‑only during the game.
    }

    @Override
    protected HBox createBottomPanel() {
        HBox statusBar = new HBox(10);
        statusBar.getStyleClass().add("status-bar");
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(10));
        statusBar.setPrefHeight(40);
        return statusBar;
    }
}
