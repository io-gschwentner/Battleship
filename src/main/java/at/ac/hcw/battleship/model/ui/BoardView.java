package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.Ship;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BoardView extends SuperBoardView{

    public BoardView(){
        super();
    }


    protected void onCellClicked(int r, int c) {

        if (currentShip == null || currentShip.isPlaced()) {
            if (setup.allShipsPlaced()) {
                startGameButton.setDisable(false);
                statusLabel.setText("All ships have been placed!");
                return;
            }
            currentShip = setup.getShips().get(shipIndex);
            shipIndex++;
            statusLabel.setText("Place " + currentShip.toString());
            return;
        }

        int length = currentShip.getLength();
        boolean horizontal = currentShip.isHorizontal();

        boolean ok = board.placeShip(r, c, length, horizontal);
        if (!ok) {
            statusLabel.setText("Invalid placement");
            return;
        }

        for (int i = 0; i < length; i++) {
            int rr = horizontal ? r : r+ i;
            int cc = horizontal ? c + i : c;

            if (rr >= 0 && rr < SIZE && cc >= 0 && cc < SIZE) {
                cellButton[rr][cc].getStyleClass().add("cell-ship");
            }
        }

        currentShip.setPlaced(true);
//        statusLabel.setText(
//                currentShip.getName() + " placed at " + (char) ('A' + r) + (c + 1));
    }

    protected HBox createBottomPanel() {
        HBox statusBar = new HBox(10);
        statusBar.getStyleClass().add("status-bar");
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(10));
        statusBar.setPrefHeight(40);

        statusLabel = new Label("Ready for ship placement!");
        statusLabel.getStyleClass().add("status-label");

        Button reset = new Button("Reset");
        reset.setPrefSize(80, 30);
        reset.setOnAction(e -> resetBoard());

        Button rotate = new Button("Rotate");
        rotate.setPrefSize(80,30);
        rotate.setOnAction(e -> changeOrientation());

        startGameButton = new Button("Start");
        startGameButton.setPrefSize(60,30);
        startGameButton.setDisable(true);
        startGameButton.setOnAction(e-> startGame());

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(leftSpacer, statusLabel, rotate, reset, startGameButton, rightSpacer);

        return statusBar;
    }

    private void resetBoard() {
        board.clearBoard();
        setup.resetBoard();
        shipIndex = 0;
        currentShip = null;

        // SET PLACED = FALSE & HORIZONTAL = TRUE
        for (Ship ship : setup.getShips()) {
            ship.setPlaced(false);
            ship.setHorizontal(true);
        }

        startGameButton.setDisable(true);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cellButton[r][c].getStyleClass().setAll("cell-button");
            }
        }

        statusLabel.setText("Ready for ship placement!");
    }

    private void startGame() {
        //disable placement
        for (int r = 0; r <SIZE; r++){
            for (int c= 0; c <SIZE; c++){
                cellButton[r][c].setDisable(true);
            }
        }

        //TODO: implement starting game
    }

    private void changeOrientation(){
        if (currentShip == null || currentShip.isPlaced()){
            if (setup.allShipsPlaced()) {
                statusLabel.setText("All ships have been placed!");
                return;
            }
            currentShip = setup.getShips().get(shipIndex);
        }

        currentShip.setHorizontal(!currentShip.isHorizontal());
        statusLabel.setText("Place " + currentShip.toString());
    }
}
