package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.CellState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EnemyBoardView extends InteractiveBoardView{
    public EnemyBoardView(GameBoard gameBoard){
        super(gameBoard);
    }

    protected void onCellClicked(int row, int col) {
        if (board.getCell(row, col) == CellState.EMPTY || board.getCell(row, col) == CellState.SHIP){
            board.fireAt(row, col);
        }
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
