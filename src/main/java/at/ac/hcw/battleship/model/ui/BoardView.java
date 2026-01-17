package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.logic.GameSetup;
import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.Ship;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BoardView extends SuperBoardView {

    public BoardView(Stage stage) {
        super(stage);
    }

    protected void onCellClicked(int r, int c) {

        if (currentShip == null || currentShip.isPlaced()) {
            if (setup.allShipsPlaced()) {
                statusLabel.setText("All ships have been placed!");
                return;
            }
            currentShip = setup.getShips().get(shipIndex);
            shipIndex++;
            statusLabel.setText("Place " + currentShip.getName());
            return;
        }

        int length = currentShip.getLength();
        boolean horizontal = true; // TODO: make this dynamic later

        boolean ok = board.placeShip(r, c, length, horizontal);
        if (!ok) {
            statusLabel.setText("Invalid placement");
            return;
        }

        for (int i = 0; i < length; i++) {
            int rr = r;
            int cc = c + i;
            if (rr >= 0 && rr < SIZE && cc >= 0 && cc < SIZE) {
                cellButton[rr][cc].getStyleClass().add("cell-ship");
            }
        }

        currentShip.setPlaced(true);
        statusLabel.setText(
                currentShip.getName() + " placed at " + (char) ('A' + r) + (c + 1)
        );

        // TODO:
        //  - support vertical placement (use ship.isHorizontal())
        //  - when all ships placed, notify Game to continue to battle phase
    }
}
