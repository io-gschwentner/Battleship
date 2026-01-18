package at.ac.hcw.battleship.model;

import at.ac.hcw.battleship.model.enums.CellState;
import at.ac.hcw.battleship.players.Coord;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class GameBoard implements Targetable{
    private final int size;
    private final ObjectProperty<CellState>[][] grid;
    private final List<Ship> ships;

    @SuppressWarnings("unchecked")
    public GameBoard(int size) {
        this.size = size;
        this.grid = new ObjectProperty[size][size];
        ships = new ArrayList<>();
        clearBoard();
    }

    public int getSize() {
        return size;
    }

    // Property access (important for binding)
    public ObjectProperty<CellState> cellProperty(int row, int col) {
        return grid[row][col];
    }

    //get the cell state
    public CellState getCell(int row, int col) {
        return grid[row][col].get();
    }

    //set every cell to EMPTY
    public void clearBoard() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = new SimpleObjectProperty<>(CellState.EMPTY);
            }
        }
    }

    //check if rows and cols are in bounds (0 and size-1)
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    //placing ships (only multi-tile ships)
    public boolean placeShip(int row, int col, int length, boolean horizontal) {
        int endRow = horizontal ? row : row + length - 1;    //find end row of the ship
        int endCol = horizontal ? col + length - 1 : col;   //find end col of the ship

        //cell validation (BOUNDS)
        if (!inBounds(row, col) || !inBounds(endRow, endCol)) {
            return false;
        }
        //validation (EMPTY?)
        for (int r = row; r <= endRow; r++) {
            for (int c = col; c <= endCol; c++) {
                if (grid[r][c].get() != CellState.EMPTY) {
                    return false;
                }
            }
        }

        //place ship
        Ship newShip = new Ship("Ship", length);
        for (int r = row; r <= endRow; r++) {
            for (int c = col; c <= endCol; c++) {
                grid[r][c].set(CellState.SHIP);
                newShip.addCoordinate(new Coord(r, c));
            }
        }
        ships.add(newShip);
        return true;
    }
    public int getRemainingShipCells() {
        int count = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c].get() == CellState.SHIP) {
                    count++;
                }
            }
        }
        return count;
    }

    public CellState fireAt(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IllegalArgumentException("Shot out of bounds");
        }

        if (grid[row][col].get() == CellState.SHIP) {
            if(ships.stream().findFirst().isPresent()) {
                grid[row][col].set(CellState.HIT);
                Ship ship = ships.stream().findFirst().get();
                ship.addHit();
                if(ship.isSunk()){
                    for(Coord coordinate : ship.getCoordinates()){
                        grid[coordinate.row][coordinate.col].set(CellState.SUNK);
                    }
                    return CellState.SUNK;
                }
                return CellState.HIT;
            }
            else {
                throw new RuntimeException();
            }
        }

        if (grid[row][col].get() == CellState.EMPTY) {
            grid[row][col].set(CellState.MISS);
            return CellState.MISS;
        }

        // already shot here
        return grid[row][col].get();
    }
}
