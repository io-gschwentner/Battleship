package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.GameBoard;
import java.util.*;

public class RandomShipPlacement {

    private static final Random random = new Random();

    public static void placeRandomShips(GameBoard board, List<Integer> shipLengths){
        for (int shipLength : shipLengths){
            placeRandomShip(board, shipLength);
        }
    }

    private static void placeRandomShip(GameBoard board, int shipLength){
        int maxAttempts = 1000;


        for (int attempt = 0; attempt < maxAttempts; attempt++){
            boolean horizontal = random.nextBoolean();
            int row = random.nextInt(board.getSize());
            int col = random.nextInt(board.getSize());

            if (board.placeShip(row, col, shipLength, horizontal)){
                return;
            }
        }
        throw new RuntimeException("Could not place ship");
    }


}
