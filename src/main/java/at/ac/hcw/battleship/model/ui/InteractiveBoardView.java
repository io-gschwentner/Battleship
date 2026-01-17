package at.ac.hcw.battleship.model.ui;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public abstract class InteractiveBoardView extends SuperBoardView {
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

                cellButton[row][col] = button;

                button.setOnAction(this::handleCellClick);

                grid.add(button, col, row);
            }
        }
        return grid;
    }

    // ---------- behavior ----------

    private void handleCellClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        String[] pos = button.getId().split(",");
        int r = Integer.parseInt(pos[0]);
        int c = Integer.parseInt(pos[1]);
        onCellClicked(r, c);
    }

    abstract void onCellClicked(int r, int c);
}
