package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class PlayerBoardView extends StaticBoardView{
    public PlayerBoardView(GameBoard gameBoard) {
        super(gameBoard);
    }

    protected HBox createBottomPanel() {
        HBox statusBar = new HBox(10);
        statusBar.getStyleClass().add("status-bar");
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(10));
        statusBar.setPrefHeight(40);

        return statusBar;
    }
}
