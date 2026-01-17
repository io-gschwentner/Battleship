// logic/Game.java
package at.ac.hcw.battleship.logic;

import at.ac.hcw.battleship.model.GameBoard;
import at.ac.hcw.battleship.players.Player;

public class Game {
    private final Player player;
    private final GameBoard playerBoard;
    private final Player enemy;
    private final GameBoard enemyBoard;
    private final WinLossService winLossService;
    private Player activePlayer;

    public Game(Player player, Player enemy, GameBoard playerBoard, GameBoard enemyBoard) {
        this.player = player;
        this.enemy = enemy;
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.winLossService = new WinLossService();
        activePlayer = enemy;
    }

    public void start() {
        do{ //game loop
            if(activePlayer == enemy){
                activePlayer = player;
                activePlayer.takeTurn(enemyBoard);
            }
            else {
                activePlayer = enemy;
                activePlayer.takeTurn(playerBoard);
            }
        }while(winLossService.evaluate(playerBoard, enemyBoard) == WinLossService.GameResult.ONGOING);
        // later: game loop, calls to applyShot(), win/loss check, opponent moves
    }

    public GameBoard getPlayerBoard() {
        return playerBoard;
    }

    public GameBoard getEnemyBoard() {
        return enemyBoard;
    }

    public WinLossService getWinLossService() {
        return winLossService;
    }
}
