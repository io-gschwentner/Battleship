package at.ac.hcw.battleship.model;

public class Ship {

    private final int length;
    private final String name;
    private boolean isPlaced = false;
    private boolean horizontal;

    public Ship(String name, int length){
        this.name = name;
        this.length = length;
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
}
