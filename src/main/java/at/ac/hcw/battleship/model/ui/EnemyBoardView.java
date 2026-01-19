package at.ac.hcw.battleship.model.ui;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.CellState;
import at.ac.hcw.battleship.players.Coord;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Board where the local human player fires at the enemy.
 * This view does not contain game logic. Instead, it delegates
 * valid clicks to a callback (onHumanShot), so the application
 * controller can:
 *  - apply the shot to the GameBoard,
 *  - trigger the AI or second player turn,
 *  - and check for win/loss.
 */
public class EnemyBoardView extends InteractiveBoardView {

    /**
     * Callback invoked when the user clicks a cell that can be shot at.
     * The Coord contains the row and column of the shot.
     */
    private Consumer<Coord> onHumanShot;

    public EnemyBoardView(GameBoard gameBoard) {
        super(gameBoard);
    }

    /**
     * Sets the callback that will be executed when the user performs
     * a valid shot (on an EMPTY or SHIP cell).
     */
    public void setOnHumanShot(Consumer<Coord> onHumanShot) {
        this.onHumanShot = Objects.requireNonNull(onHumanShot, "onHumanShot must not be null");
    }

    @Override
    protected void onCellClicked(int row, int col) {
        System.out.println("Enemy cell clicked: " + row + "," + col);
        CellState cell = board.getCell(row, col);

        // Only allow shots on cells that have not been shot before.
        if ((cell == CellState.EMPTY || cell == CellState.SHIP) && onHumanShot != null) {
            onHumanShot.accept(new Coord(row, col));
        }
    }

    @Override
    protected HBox createBottomPanel() {
        HBox statusBar = new HBox(10);
        statusBar.getStyleClass().add("status-bar");
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(10));
        statusBar.setPrefHeight(40);
        return statusBar;
    }
}
