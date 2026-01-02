package at.ac.hcw.battleship;

import at.ac.hcw.battleship.logic.Game;
import at.ac.hcw.battleship.model.Board;

public class Program {
    public static void main(String[] args) {

            Board playerBoard = new Board(10, 10);
            Board enemyBoard = new Board(10, 10);

            Game game = new Game(playerBoard, enemyBoard);
            game.start();
        }
    }

