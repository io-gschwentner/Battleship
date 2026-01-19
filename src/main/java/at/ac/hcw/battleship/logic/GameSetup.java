package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.Ship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the list of ships that must be placed for one player
 * and tracks which ships are already placed.
 */
public class GameSetup {

    /**
     * Immutable template list of all ships in a standard game.
     */
    private final List<Ship> originalShips;

    /**
     * Mutable working list used during placement.
     */
    private List<Ship> ships;

    public GameSetup() {
        this.originalShips = initializeOriginalShips();
        this.ships = copyShipTemplates(originalShips);
    }

    /**
     * Defines the fleet: 2 destroyers (2), 3 carriers (3), 2 battleships (4).
     */
    private List<Ship> initializeOriginalShips() {
        List<Ship> list = new ArrayList<>();
        list.add(new Ship("Destroyer 1", 2));
        list.add(new Ship("Destroyer 2", 2));
        list.add(new Ship("Carrier 1", 3));
        list.add(new Ship("Carrier 2", 3));
        list.add(new Ship("Carrier 3", 3));
        list.add(new Ship("Battleship 1", 4));
        list.add(new Ship("Battleship 2", 4));
        return Collections.unmodifiableList(list);
    }

    /**
     * Creates fresh ship instances based on a template list.
     * This avoids reusing state (hits/placement/orientation) when resetting.
     */
    private List<Ship> copyShipTemplates(List<Ship> templates) {
        List<Ship> list = new ArrayList<>();
        for (Ship template : templates) {
            list.add(new Ship(template.getName(), template.getLength()));
        }
        return list;
    }

    /**
     * Returns the current working list of ships (for one player).
     */
    public List<Ship> getShips() {
        return ships;
    }

    /**
     * Returns true if all ships have been placed on the board.
     */
    public boolean allShipsPlaced() {
        for (Ship ship : ships) {
            if (!ship.isPlaced()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Resets the setup to the original fleet configuration,
     * clearing placement state and orientation.
     */
    public void reset() {
        this.ships = copyShipTemplates(originalShips);
    }
}
