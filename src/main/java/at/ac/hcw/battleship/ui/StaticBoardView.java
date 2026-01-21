package at.ac.hcw.battleship.ui;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.CellState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Read‑only board view (no click handling).
 */
public abstract class StaticBoardView extends SuperBoardView {

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
                button.getStyleClass().add("cell-button");
                button.getStyleClass().add("no-hover");

                board.cellProperty(row, col).addListener((obs, oldState, newState) -> {
                    button.getStyleClass().removeAll(
                            "cell-empty", "cell-ship", "cell-hit", "cell-miss", "cell-sunk"
                    );
                    applyCellStyle(button, newState);
                });

                applyCellStyle(button, board.getCell(row, col));

                cellButton[row][col] = button;
                grid.add(button, col, row);
            }
        }
        return grid;
    }

    protected void applyCellStyle(Button button, CellState state) {
        switch (state) {
            case EMPTY -> button.getStyleClass().add("cell-empty");
            case SHIP -> button.getStyleClass().add("cell-ship");
            case HIT -> button.getStyleClass().add("cell-hit");
            case MISS -> button.getStyleClass().add("cell-miss");
            case SUNK -> button.getStyleClass().add("cell-sunk");
        }
    }
}
