package client;
import java.awt.*;

public class Player {
    private int x;
    private int score;
    private String username;
    private String status;

    public Player(String username, String status) {
        this.username = username;
        this.status = status;
        this.x = 200; // Vị trí ban đầu của thùng rác
        this.score = 0;
    }

    public void moveLeft() {
        if (x > 0) {
            x -= 50; // Di chuyển sang trái
        }
    }

    public void moveRight() {
        if (x < 350) {
            x += 50; // Di chuyển sang phải
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, 550, 100, 30); // Vẽ thùng rác
        g.setColor(Color.BLACK);
        g.drawString(username, x + 20, 570); // Tên người chơi
    }

    public int getX() {
        return x;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int value) {
        score += value;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() { return status;
    }
}
