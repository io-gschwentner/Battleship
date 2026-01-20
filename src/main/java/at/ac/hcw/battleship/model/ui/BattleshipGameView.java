package at.ac.hcw.battleship.model.ui;

public interface BattleshipGameView {
    void setStatus(String message);
    void updateStats(int hits, int misses, int enemyShipsRemaining);
    void disableInteractions();
}
