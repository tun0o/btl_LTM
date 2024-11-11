package client.GameUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;
import java.net.URL; // Thêm dòng này

//private static int globalSpeed = 10; // Tốc độ gốc cho vật phẩm đầu tiên

public class Trash extends JLabel {
    private int yPosition;
    private float speed;
    private String type;
    private String name;
    private Random random = new Random();
    public static float initialSpeed = 5; // Tốc độ rơi ban đầu

    // Đường dẫn đến các thư mục chứa ảnh
    private static final String IMAGE_PATH_PLASTIC = "/resources/plastic";
    private static final String IMAGE_PATH_METAL = "/resources/metal";
    private static final String IMAGE_PATH_PAPER = "/resources/paper";
    private static final String IMAGE_PATH_ORGANIC = "/resources/organic";

    public Trash() {
        setBounds(375, 0, 120, 120); // Vị trí x ở giữa màn hình


        resetPosition();

    }

    public void resetPosition() {
        // Đặt rác lại lên trên cùng và chọn vị trí x cố định ở giữa
        yPosition = 0;
        setLocation(375, yPosition); // Giữ rác ở giữa màn hình
        speed = initialSpeed; // Đặt tốc độ ban đầu
        initialSpeed+=0.5f;
        type = generateRandomType(); // Tạo loại rác ngẫu nhiên
        name = generateRandomName(type); // Tạo tên cho loại rác
        setIcon(generateRandomImage(type)); // Đặt ảnh ngẫu nhiên cho loại rác

    }

    public void moveDown(int timeElapsed) {
        // Tăng tốc độ theo thời gian đã trôi qua
//        speed = initialSpeed + timeElapsed / 10; // Tăng tốc độ theo thời gian
        speed = initialSpeed;
        yPosition += speed;
        setLocation(getX(), yPosition);
    }

    public boolean reachedBottom() {
        return yPosition >= 450; // Điều chỉnh lại vị trí khi rác chạm đáy (dưới cùng)
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private String generateRandomType() {
        String[] types = {"plastic", "metal", "paper", "organic"};
        return types[random.nextInt(types.length)];
    }

    private String generateRandomName(String type) {
        switch (type) {
            case "plastic":
                return "Bottle";
            case "metal":
                return "Can";
            case "paper":
                return "Newspaper";
            case "organic":
                return "Fruit Peel";
            default:
                return "Unknown";
        }
    }

    private ImageIcon generateRandomImage(String type) {
//        String folderPath = getFolderPathForType(type);
//        File folder = new File(folderPath);
//        File[] imageFiles = folder.listFiles();
//
//
//        if (imageFiles != null && imageFiles.length > 0) {
//            // Chọn ngẫu nhiên một file ảnh từ thư mục
//            File randomImageFile = imageFiles[random.nextInt(imageFiles.length)];
//            return new ImageIcon(randomImageFile.getAbsolutePath());
//        } else {
//            // Nếu không có ảnh, hiển thị ảnh mặc định
//            return new ImageIcon("path/to/default/image.png");
//        }

        String folderPath = getFolderPathForType(type);
        try {
            // Lấy đường dẫn thư mục tương ứng với loại rác
            URL folderUrl = getClass().getResource(folderPath);
            File folder = new File(folderUrl.toURI());
            File[] imageFiles = folder.listFiles((dir, name) -> name.endsWith(".png"));

            if (imageFiles != null && imageFiles.length > 0) {
                // Chọn ngẫu nhiên một file ảnh từ thư mục
                File randomImageFile = imageFiles[random.nextInt(imageFiles.length)];
//                return new ImageIcon(randomImageFile.getAbsolutePath());
                ImageIcon icon = new ImageIcon(randomImageFile.getAbsolutePath());
                Image image = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Điều chỉnh kích thước
                setIcon(new ImageIcon(image)); // Gán icon đã được điều chỉnh kích thước

                return new ImageIcon(image);

            } else {
                // Nếu không có ảnh, hiển thị ảnh mặc định
                return new ImageIcon(getClass().getResource("/path/to/default/image.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ImageIcon(getClass().getResource("/path/to/default/image.png")); // Ảnh mặc định nếu có lỗi
        }

    }

    private String getFolderPathForType(String type) {
        switch (type) {
            case "plastic":
                return IMAGE_PATH_PLASTIC;
            case "metal":
                return IMAGE_PATH_METAL;
            case "paper":
                return IMAGE_PATH_PAPER;
            case "organic":
                return IMAGE_PATH_ORGANIC;
            default:
                return "path/to/default"; // Đường dẫn mặc định nếu không có loại phù hợp
        }
    }
}
