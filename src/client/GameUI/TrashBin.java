package client.GameUI;

import javax.swing.*;
import java.awt.*;

public class TrashBin extends JPanel {
    private String type;
    private JLabel nameLabel;
    private ImageIcon binImage;
    private boolean selected = false; // Biến để xác định xem thùng rác có được chọn hay không

    public TrashBin(int x, int y, String type, String name) {
        this.type = type;
        setBounds(x, y, 100, 100);
        setOpaque(false);  // Làm cho JPanel trong suốt

        // Tạo nhãn để hiển thị tên thùng rác
        nameLabel = new JLabel(name);
        nameLabel.setForeground(Color.WHITE); // Đặt màu chữ
        nameLabel.setBounds(10, 10, 80, 20);
        add(nameLabel);

        // Tải hình ảnh cho thùng rác
        binImage = new ImageIcon(getClass().getResource(getImagePath(type)));
    }

    // Phương thức để lấy đường dẫn hình ảnh
    private String getImagePath(String type) {
        switch (type) {
            case "paper":
                return "/resources/bin/paper_bin.png";
            case "plastic":
                return "/resources/bin/plastic_bin.png";
            case "metal":
                return "/resources/bin/metal_bin.png";
            case "organic":
                return "/resources/bin/organic_bin.png";
            default:
                return "/resources/default_bin.png";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // Vẽ hình ảnh thùng rác với kích thước lớn hơn nếu đang được chọn
        if (binImage != null) {
            int width = getWidth();
            int height = getHeight();

            if (selected) {
//                width += 20;  // Tăng kích thước hình ảnh nếu được chọn
//                height += 20;
                int shadowSize = 10; // Độ lớn của đổ bóng
                g2d.setColor(new Color(0, 255, 0, 100)); // Màu xanh lá mờ cho bóng
                g2d.fillOval(-shadowSize / 2, -shadowSize / 2, getWidth() + shadowSize, getHeight() + shadowSize);

            }
//            g.drawImage(binImage.getImage(), 0, 0, width, height, this);
            g2d.drawImage(binImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Phương thức để xác định thùng rác có đúng loại hay không
    public boolean isCorrectBin(Trash trash) {
        return type.equals(trash.getType());
    }

    // Phương thức để thay đổi trạng thái chọn
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint(); // Gọi lại paintComponent để vẽ lại kích thước
    }

    public void rotate() {
        setSelected(true); // Đặt thùng rác ở trạng thái đã chọn
    }

    public void reset() {
        setSelected(false); // Trở lại trạng thái mặc định
    }
}
