package client;
import java.awt.*;

public class TrashItem {
    private int x, y;
    private int speed;
    private String type;

    public TrashItem(int x, int speed, String type) {
        this.x = x;
        this.y = 0; // Bắt đầu từ trên cùng
        this.speed = speed;
        this.type = type;
    }

    public void move() {
        y += speed; // Di chuyển xuống dưới
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 30, 30); // Vẽ vật phẩm rác
        g.setColor(Color.BLACK);
        g.drawString(type, x + 10, y + 20); // Hiển thị loại rác
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public String getType() {
        return type;
    }
}
