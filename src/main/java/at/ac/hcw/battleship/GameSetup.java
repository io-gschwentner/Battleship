package at.ac.hcw.battleship;

import java.util.ArrayList;
import java.util.List;

public class GameSetup {

/* |SHIPS|
    - 1 Destroyer (1x2)
    - 3 Carrier (1x3)
    - 2 Battleships (1x4)
 */

    private List<Ship> originalShips;
    private List<Ship> ships;


    public GameSetup() {
        originalShips = initializeOriginalShips();
        ships = new ArrayList<>(originalShips);

    }

    public List<Ship> initializeOriginalShips(){
        List <Ship> ships = new ArrayList<>();
        ships.add(new Ship("Destroyer 1", 2));
        ships.add(new Ship("Destroyer 2", 2));
        ships.add(new Ship("Carrier 1", 3));
        ships.add(new Ship("Carrier 2", 3));
        ships.add(new Ship("Carrier 3", 3));
        ships.add(new Ship("Battleship 1", 4));
        ships.add(new Ship("Battleship 2", 4));
        return ships;
    }


    public List<Ship> getShips(){return ships;}


    public boolean allShipsPlaced(){
        for (Ship ship : ships) {
            if (!ship.isPlaced())
                return false;
        }
        return true;
    }

    //reset ship list
    public void resetBoard(){
        ships = new ArrayList<>(originalShips);
    }
}
