package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.Targetable;
import at.ac.hcw.battleship.model.enums.CellState;
import at.ac.hcw.battleship.model.enums.KnownCellState;

import java.util.*;

/**
 * Medium AI using a hunt-and-target strategy:
 * - Hunt: checkerboard pattern to find ships.
 * - Target: when a hit is found, shoot around it using a queue.
 */
public class MediumAiPlayer implements Player {

    private final KnownGameBoard gameState;
    private final Random random = new Random();
    private final Deque<Coord> targetQueue = new ArrayDeque<>();

    public MediumAiPlayer(KnownGameBoard gameState) {
        this.gameState = gameState;
    }

    @Override
    public void takeTurn(Targetable targetableGameBoard) {
        Coord shot = chooseShot();
        CellState result = targetableGameBoard.fireAt(shot.row, shot.col);
        processResult(shot, result);
    }

    private Coord chooseShot() {
        if (!targetQueue.isEmpty()) {
            return targetQueue.poll();
        }
        return huntShot();
    }

    /**
     * Uses a checkerboard pattern first, then falls back to all UNKNOWN cells.
     */
    private Coord huntShot() {
        List<Coord> candidates = new ArrayList<>();

        int size = gameState.getSize();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (gameState.getCell(row, col) == KnownCellState.UNKNOWN
                        && (row + col) % 2 == 0) {
                    candidates.add(new Coord(row, col));
                }
            }
        }

        if (candidates.isEmpty()) {
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if (gameState.getCell(row, col) == KnownCellState.UNKNOWN) {
                        candidates.add(new Coord(row, col));
                    }
                }
            }
        }

        int index = random.nextInt(candidates.size());
        return candidates.get(index);
    }

    private void processResult(Coord shot, CellState result) {
        if (result == CellState.HIT) {
            gameState.setCell(shot.row, shot.col, KnownCellState.HIT);
            enqueueAdjacent(shot);
        } else if (result == CellState.SUNK) {
            gameState.setCell(shot.row, shot.col, KnownCellState.HIT);
            targetQueue.clear();
        } else if (result == CellState.MISS) {
            gameState.setCell(shot.row, shot.col, KnownCellState.MISS);
        }
    }

    /**
     * Adds adjacent UNKNOWN cells to the target queue.
     */
    private void enqueueAdjacent(Coord hit) {
        int[][] dirs = {
                {-1, 0}, {1, 0},
                {0, -1}, {0, 1}
        };

        for (int[] d : dirs) {
            int nextRow = hit.row + d[0];
            int nextCol = hit.col + d[1];

            if (inBounds(nextRow, nextCol)
                    && gameState.getCell(nextRow, nextCol) == KnownCellState.UNKNOWN) {
                targetQueue.add(new Coord(nextRow, nextCol));
            }
        }
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < gameState.getSize()
                && col >= 0 && col < gameState.getSize();
    }
}
