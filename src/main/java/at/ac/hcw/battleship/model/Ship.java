package at.ac.hcw.battleship.model;

import at.ac.hcw.battleship.players.Coord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single ship with a name, length and board coordinates.
 */
public class Ship {

    private final int length;
    private final String name;
    private boolean placed;
    private boolean horizontal;
    private final List<Coord> coordinates;
    private int hits;

    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
        this.coordinates = new ArrayList<>();
        this.horizontal = true;
        this.hits = 0;
        this.placed = false;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    /**
     * Immutable view of the coordinates occupied by this ship.
     */
    public List<Coord> getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    public void addCoordinate(Coord coordinate) {
        coordinates.add(coordinate);
    }

    public void addHit() {
        hits++;
    }

    public boolean isSunk() {
        return hits >= length;
    }

    @Override
    public String toString() {
        return name + " (" + length + (horizontal ? " Horizontal)" : " Vertical)");
    }
}
