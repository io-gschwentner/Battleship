package at.ac.hcw.battleship.players;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.model.enums.CellState;

import java.util.Scanner;

public class HumanPlayer implements Player{
    private final Scanner scanner = new Scanner(System.in);

    public HumanPlayer() { }

    @Override
    public void takeTurn(GameBoard enemyBoard) {
        int row;
        int col;

        while (true) {
            System.out.print("Enter row: ");
            row = scanner.nextInt();

            System.out.print("Enter column: ");
            col = scanner.nextInt();

            if (isValidShot(enemyBoard, row, col)) {
                break;
            }

            System.out.println("Invalid shot. Try again.");
        }

        CellState result = enemyBoard.fireAt(row, col);

        displayShotResult(result);
    }

    private boolean isValidShot(GameBoard enemyBoard, int row, int col) {
        if (row < 0 || row >= enemyBoard.getSize() || col < 0 || col >= enemyBoard.getSize()) {
            return false;
        }

        CellState cell = enemyBoard.getCell(row, col);
        return cell == CellState.EMPTY || cell == CellState.SHIP;
    }

    private void displayShotResult(CellState result) {
        if (result == CellState.HIT) {
            System.out.println("Hit!");
        } else if (result == CellState.MISS) {
            System.out.println("Miss.");
        }
    }
}
