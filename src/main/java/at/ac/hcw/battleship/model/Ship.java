package at.ac.hcw.battleship.model;

import at.ac.hcw.battleship.players.Coord;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private final int length;
    private final String name;
    private boolean isPlaced = false;
    private boolean horizontal;
    private final List<Coord> coordinates;
    private int hits;

    public Ship(String name, int length){
        this.name = name;
        this.length = length;
        this.coordinates = new ArrayList<>();
        this.horizontal = true;
        this.hits = 0;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public boolean isPlaced(){
        return isPlaced;
    }

    public void setPlaced(boolean isPlaced){
        this.isPlaced = isPlaced;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public List<Coord> getCoordinates() { return coordinates; }

    public void addCoordinate(Coord coordinate) { coordinates.add(coordinate); }

    public void addHit() { hits = hits + 1; }

    public boolean isSunk() {
        return length == hits;
    }

    @Override
    public String toString (){
        return name + " (" + length + (isHorizontal() ? " Horizontal)" : " Vertical)");
    }
}
