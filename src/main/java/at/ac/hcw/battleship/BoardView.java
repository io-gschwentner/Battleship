package at.ac.hcw.battleship;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.*;

public class BoardView {

    public static final int SIZE = 10;
    public static final int CELL_SIZE = 40;

    private GameBoard board;
    private GameSetup setup;
    private Button[][] cellButton;
    private int shipIndex = 0;
    private Ship currentShip;
    private Label statusLabel;


    public void showView(Stage stage){
        board = new GameBoard(SIZE);
        setup = new GameSetup();
        cellButton = new Button[SIZE][SIZE];

        BorderPane root = createRoot();
        Scene scene = new Scene(root, 500,550);
        stage.setScene(scene);
        stage.setTitle("Place your ships");
        stage.show();
    }

    //Layout
    private BorderPane createRoot(){
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        //main panels
        HBox topPanel = createColumnLabels();
        VBox leftPanel = createRowLabels();
        GridPane grid = createGrid();
        HBox bottomPanel = createBottomPanel();

        BorderPane.setAlignment(topPanel, Pos.CENTER_RIGHT);
        BorderPane.setAlignment(leftPanel, Pos.CENTER_LEFT);
        topPanel.setPrefHeight(25);
        topPanel.setMaxHeight(25);
        topPanel.setPadding(new Insets(50,0,0,50));
        leftPanel.setPadding(new Insets(0,0,0,25));
        grid.setPadding(new Insets(0,25,0,0));
        bottomPanel.setPadding(new Insets(0,0,50,0));
        HBox.setHgrow(topPanel, Priority.NEVER);
        BorderPane.setAlignment(grid, Pos.CENTER);
        grid.setPrefSize(420,420);
        grid.setMaxSize(420,420);
        BorderPane.setAlignment(bottomPanel, Pos.CENTER);

        BorderPane.setMargin(topPanel, new Insets(0,0,5,0));
        BorderPane.setMargin(leftPanel, new Insets(0,0,0,5));
        BorderPane.setMargin(bottomPanel, new Insets(5,0,0,0));

        root.setTop(topPanel);
        root.setLeft(leftPanel);
        root.setCenter(grid);
        root.setBottom(bottomPanel);

        return root;
    }
    //resste method
    private void resetBoard(){
        board.clearBoard();
        setup.resetBoard();
        shipIndex = 0;
        for (Ship ship : setup.getShips()){
            ship.setPlaced(false);
        }
        currentShip = null;

        for (int r = 0; r < SIZE; r++){
            for (int c = 0; c < SIZE; c++){
                cellButton[r][c].setStyle("");
            }
        }
        statusLabel.setText("Ready for ship placement!");
    }

    private HBox createColumnLabels(){
        HBox labels = new HBox();
        labels.setAlignment(Pos.CENTER);

        for (int col = 1; col <= SIZE; col++){
            Label label = new Label(String.valueOf(col));
            label.setPrefSize(40, 25);
            label.setMinSize(40,25);
            label.setMaxWidth(40);
            HBox.setHgrow(label, Priority.NEVER);
            labels.getChildren().add(label);
        }
        return labels;
    }

    private VBox createRowLabels(){
        VBox labels = new VBox(2);
        labels.setAlignment(Pos.CENTER_RIGHT);
        labels.setPadding(new Insets(5, 0, 5, 0));

        for (int row = 0; row < SIZE; row++){
            char rowLetter = (char)('A' + row);
            Label label = new Label(String.valueOf(rowLetter));
            label.setPrefSize(25, CELL_SIZE);
            label.setMinSize(25,CELL_SIZE);
            VBox.setVgrow(label, Priority.NEVER);
            labels.getChildren().add(label);
        }
        return labels;
    }

    private HBox createBottomPanel(){
        HBox statusBar = new HBox(10);
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(10));
        statusBar.setPrefHeight(40);
        statusBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 0 0 2 0;");

        statusLabel = new Label("Ready for ship placement!");

        Button reset = new Button("Reset");
        reset.setPrefSize(80,30);
        reset.setOnAction(e -> resetBoard());
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        statusBar.getChildren().addAll(leftSpacer, statusLabel, reset);

        return statusBar;
    }
    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(2);
        grid.setVgap(2);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setId(row + "," + col);

                cellButton[row][col] = button;

                //on click, return
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String[] pos = button.getId().split(",");
                        int r = Integer.parseInt(pos[0]);
                        int c = Integer.parseInt(pos[1]);
                        onCellClicked(r,c);
                    }
                });
                grid.add(button, col, row);
            }
        }
        return grid;
    }
    //should this go in game setup? vvv
    private void onCellClicked ( int r, int c){

        if (currentShip == null || currentShip.isPlaced()){
            if (setup.allShipsPlaced()){
                statusLabel.setText("All ships have been placed!");
                return;
            }
            currentShip = setup.getShips().get(shipIndex);
            shipIndex++;
            statusLabel.setText("Place " + currentShip.getName());
            return;
        }

        int length = currentShip.getLength();
        boolean horizontal = true;

        boolean ok = board.placeShip(r,c,length, true);
        if (!ok){
            statusLabel.setText("Invalid placement");
            return;
        }

        for (int i = 0; i < length; i++){
            int rr = r;
            int cc = c + i;
            if (rr >= 0 && rr < SIZE && cc >= 0 && cc < SIZE){
                cellButton[rr][cc].setStyle("-fx-background-color: navy");
            }
        }

        currentShip.setPlaced(true);
        statusLabel.setText(currentShip.getName() + " placed at " + (char)('A'+ r) + (c + 1));


/*        TODO: place ships vertically
                set up enemy game board (random)
                finalize selection --> start game
 */

    }
}
