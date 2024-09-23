package client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController extends KeyAdapter {
    private GameUI gameUI;

    public GameController(GameUI gameUI) {
        this.gameUI = gameUI;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            gameUI.moveBin(-1); // Di chuyển sang trái
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            gameUI.moveBin(1); // Di chuyển sang phải
        }
    }
}
