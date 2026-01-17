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

public abstract class SuperBoardView {

    public static final int SIZE = 10;
    public static final int CELL_SIZE = 40;

    protected final GameBoard board;
    protected final GameSetup setup;
    protected final Button[][] cellButton;

    protected int shipIndex = 0;
    protected Ship currentShip;
    protected Label statusLabel;
    protected Button startGameButton;

    public SuperBoardView() {
        this.board = new GameBoard(SIZE);
        this.setup = new GameSetup();
        this.cellButton = new Button[SIZE][SIZE];
    }

    public GameBoard getBoard() {
        return board;
    }

    public GameSetup getSetup() {
        return setup;
    }

    // ---------- layout building ----------

    public BorderPane createRoot() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox topPanel = createColumnLabels();
        VBox leftPanel = createRowLabels();
        GridPane grid = createGrid();
        HBox bottomPanel = createBottomPanel();

        BorderPane.setAlignment(topPanel, Pos.CENTER_RIGHT);
        BorderPane.setAlignment(leftPanel, Pos.CENTER_LEFT);
        BorderPane.setAlignment(grid, Pos.CENTER);
        BorderPane.setAlignment(bottomPanel, Pos.CENTER);

        topPanel.setPrefHeight(25);
        topPanel.setMaxHeight(25);
        topPanel.setPadding(new Insets(50, 0, 0, 50));

        leftPanel.setPadding(new Insets(0, 0, 0, 25));
        grid.setPadding(new Insets(0, 25, 0, 0));
        grid.setPrefSize(420, 420);
        grid.setMaxSize(420, 420);

        bottomPanel.setPadding(new Insets(0, 0, 50, 0));

        BorderPane.setMargin(topPanel, new Insets(0, 0, 5, 0));
        BorderPane.setMargin(leftPanel, new Insets(0, 0, 0, 5));
        BorderPane.setMargin(bottomPanel, new Insets(5, 0, 0, 0));

        root.setTop(topPanel);
        root.setLeft(leftPanel);
        root.setCenter(grid);
        root.setBottom(bottomPanel);

        return root;
    }

    private HBox createColumnLabels() {
        HBox labels = new HBox();
        labels.setAlignment(Pos.CENTER);

        for (int col = 1; col <= SIZE; col++) {
            Label label = new Label(String.valueOf(col));
            label.setPrefSize(CELL_SIZE, 25);
            label.setMinSize(CELL_SIZE, 25);
            label.setMaxWidth(CELL_SIZE);
            HBox.setHgrow(label, Priority.NEVER);
            labels.getChildren().add(label);
        }
        return labels;
    }

    private VBox createRowLabels() {
        VBox labels = new VBox(2);
        labels.setAlignment(Pos.CENTER_RIGHT);
        labels.setPadding(new Insets(5, 0, 5, 0));

        for (int row = 0; row < SIZE; row++) {
            char rowLetter = (char) ('A' + row);
            Label label = new Label(String.valueOf(rowLetter));
            label.setPrefSize(25, CELL_SIZE);
            label.setMinSize(25, CELL_SIZE);
            VBox.setVgrow(label, Priority.NEVER);
            labels.getChildren().add(label);
        }
        return labels;
    }

    abstract HBox createBottomPanel();

    private GridPane createGrid() {
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
