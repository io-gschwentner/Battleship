package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.GameBoard;
import java.util.*;

public class RandomShipPlacement {

    public static void placeRandomShips(GameBoard board, List<Integer> shipLengths){
        for (int shipLength : shipLengths){
            placeRandomShip(board, shipLength);
        }
    }

    private static void placeRandomShip(GameBoard board, int shipLength){
        Random random = new Random();
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
