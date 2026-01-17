package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.CellState;
import at.ac.hcw.battleship.model.enums.KnownCellState;
import java.util.*;

//hunt and target strategy
public class MediumAiPlayer implements Player {

    private final KnownGameBoard gameState;
    private final Random random = new Random(System.currentTimeMillis());

    private final Deque<Coord> targetQueue = new ArrayDeque<>(); //Double ended queue

    public MediumAiPlayer(KnownGameBoard gameState) {
        this.gameState = gameState;
    }

    @Override
    public void takeTurn(GameBoard enemyBoard) { //checkerboard and sink strat
        Coord shot = chooseShot();

        CellState result = enemyBoard.fireAt(shot.row, shot.col);

        processResult(shot, result);
    }

    private Coord chooseShot() {
        if (!targetQueue.isEmpty()) {
            return targetQueue.poll();
        }

        return huntShot();
    }

    private Coord huntShot() {
        List<Coord> candidates = new ArrayList<>();

        for (int row = 0; row < gameState.getSize(); row++) {
            for (int col = 0; col < gameState.getSize(); col++) {
                if (gameState.getCell(row, col) == KnownCellState.UNKNOWN
                        && (row + col) % 2 == 0) {
                    candidates.add(new Coord(row, col));
                }
            }
        }

        // fallback if checkerboard exhausted
        if (candidates.isEmpty()) {
            for (int row = 0; row < gameState.getSize(); row++) {
                for (int col = 0; col < gameState.getSize(); col++) {
                    if (gameState.getCell(row, col) == KnownCellState.UNKNOWN) {
                        candidates.add(new Coord(row, col));
                    }
                }
            }
        }

        return candidates.get(random.nextInt(candidates.size()));
    }

    private void processResult(Coord shot, CellState result) {
        if (result == CellState.HIT) {
            gameState.setCell(shot.row, shot.col, KnownCellState.HIT);
            enqueueAdjacent(shot);
        } else if (result == CellState.MISS) {
            gameState.setCell(shot.row, shot.col, KnownCellState.MISS);
        }
    }

    private void enqueueAdjacent(Coord hit) {
        int[][] dirs = {
                {-1, 0}, {1, 0},
                {0, -1}, {0, 1}
        };

        for (int[] d : dirs) {
            int nextRow = hit.row + d[0];
            int nextCol = hit.col + d[1];

            if (inBounds(nextRow, nextCol) && gameState.getCell(nextRow, nextCol) == KnownCellState.UNKNOWN) {
                targetQueue.add(new Coord(nextRow, nextCol));
            }
        }
        //TODO: if sunk clear queue
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < gameState.getSize() && col >= 0 && col < gameState.getSize();
    }
}
