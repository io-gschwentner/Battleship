package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public abstract class StaticBoardView extends SuperBoardView{
    public StaticBoardView(GameBoard gameBoard) {
        super(gameBoard);
    }

    @Override
    protected GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(2);
        grid.setVgap(2);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(CELL_SIZE, CELL_SIZE);
                button.setId(row + "," + col);

                // add CSS style class for all cells
                button.getStyleClass().add("cell-button");
                button.getStyleClass().add("no-hover");

                // binding
                board.cellProperty(row, col).addListener((obs, oldState, newState) -> {
                    button.getStyleClass().removeAll(
                            "cell-empty", "cell-ship", "cell-hit", "cell-miss", "cell-sunk"
                    );

                    switch (newState) {
                        case EMPTY -> button.getStyleClass().add("cell-empty");
                        case SHIP  -> button.getStyleClass().add("cell-ship");
                        case HIT   -> button.getStyleClass().add("cell-hit");
                        case MISS  -> button.getStyleClass().add("cell-miss");
                        case SUNK  -> button.getStyleClass().add("cell-sunk");
                    }
                });

                // initialize once
                switch (board.getCell(row, col)) {
                    case EMPTY -> button.getStyleClass().add("cell-empty");
                    case SHIP  -> button.getStyleClass().add("cell-ship");
                    case HIT   -> button.getStyleClass().add("cell-hit");
                    case MISS  -> button.getStyleClass().add("cell-miss");
                    case SUNK  -> button.getStyleClass().add("cell-sunk");
                }

                cellButton[row][col] = button;
                grid.add(button, col, row);
            }
        }
        return grid;
    }
}
