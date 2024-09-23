package client;

import javax.swing.*;
import java.awt.*;

public class TrashBin extends JPanel {
    private String type;
    private JLabel nameLabel;
    private ImageIcon binImage;

    public TrashBin(int x, int y, String type, String name) {
        this.type = type;
        setBounds(x, y, 100, 100);
//        setBackground(Color.GRAY);
//        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Tạo nhãn để hiển thị tên thùng rác
        nameLabel = new JLabel(name);
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
                return "/resources/default_bin.png"; // Hình ảnh mặc định nếu không có loại phù hợp
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Vẽ hình ảnh thùng rác
        if (binImage != null) {
            g.drawImage(binImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public boolean isCorrectBin(Trash trash) {
        return type.equals(trash.getType());
    }

    public void rotate() {
        setBackground(Color.GREEN); // Màu sắc khi đang được chọn
        // Có thể thêm logic để xoay thùng rác tại đây
    }

    public void reset() {
        setBackground(Color.GRAY); // Trở lại màu mặc định
    }
}
