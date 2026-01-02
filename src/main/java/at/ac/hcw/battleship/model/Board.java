package at.ac.hcw.battleship.model;

public class Board {

        private final char[][] grid;

        public Board(int rows, int cols) {
            this.grid = new char[rows][cols];
            // Michaela: init with water, place ships, etc.
        }

        public int getRemainingShipCells() {
            // Michaela: count 'S' cells in grid and return
            return 0;
        }

        public boolean applyShot(int row, int col) {
            // Michaela: update grid and return hit/miss
            return false;
        }

        public char[][] getGrid() {
            return grid;
        }
}
