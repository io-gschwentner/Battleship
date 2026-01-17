package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.CellState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PlayerBoardView extends StaticBoardView{
    public PlayerBoardView(GameBoard gameBoard) {
        super(gameBoard);
    }

    @Override
    protected GridPane createGrid(){
        GridPane gridPane = super.createGrid();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(board.getCell(i,j) == CellState.SHIP){
                    cellButton[i][j].getStyleClass().add("cell-ship");
                }
            }
        }
        return gridPane;
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
