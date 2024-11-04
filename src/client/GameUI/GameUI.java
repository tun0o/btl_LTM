package client.GameUI;
import client.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameUI extends JPanel {
    private JLabel playerScoreLabel, opponentScoreLabel, timeLabel;
    private int playerScore = 0;
    private int opponentScore = 0;
    private int timeRemaining = 60;
    private TrashBin[] bins = new TrashBin[4];
    public int currentBinIndex = 1; // Thùng ở giữa (nhựa) bắt đầu là chỉ số 1
    private Trash trash;
    private Timer countdownTimer; // Khai báo biến ở cấp độ lớp
    private Timer gameTimer; // Khai báo biến ở cấp độ lớp



    private Image background;
    private Player currentPlayer; // Người chơi hiện tại
    private Player opponentPlayer; // Đối thủ

    public GameUI(Player currentPlayer, Player opponentPlayer, String serverAddress, int port) {
        this.currentPlayer = currentPlayer;
        this.opponentPlayer = opponentPlayer;
        try {
            background = ImageIO.read(getClass().getResource("/resources/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLayout(null);
        setPreferredSize(new Dimension(900, 600));


        // Thiết lập font chữ lớn hơn
        Font largeFont = new Font("Arial", Font.BOLD, 24);

        // Thông tin người chơi
        playerScoreLabel = new JLabel(currentPlayer.getUsername() + ": " + currentPlayer.getScore());
        playerScoreLabel.setFont(largeFont); // Thiết lập font chữ lớn hơn
        playerScoreLabel.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        playerScoreLabel.setBounds(50, 20, 300, 50); // Điều chỉnh kích thước và vị trí
        add(playerScoreLabel);

        // Thông tin đối thủ
        opponentScoreLabel = new JLabel(opponentPlayer.getUsername() + ": " + opponentPlayer.getScore());
        opponentScoreLabel.setFont(largeFont); // Thiết lập font chữ lớn hơn
        opponentScoreLabel.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        opponentScoreLabel.setBounds(500, 20, 300, 50); // Điều chỉnh kích thước và vị trí
        add(opponentScoreLabel);

        // Thời gian
        timeLabel = new JLabel("Time: 60s");
        timeLabel.setFont(largeFont); // Thiết lập font chữ lớn hơn
        timeLabel.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        timeLabel.setBounds(350, 20, 200, 50); // Điều chỉnh kích thước và vị trí
        add(timeLabel);

        // Tạo 4 thùng rác với các loại và tên khác nhau
        String[] types = {"paper", "plastic", "metal", "organic"};
        String[] names = {"Giấy", "Nhựa", "Kim loại", "Hữu cơ"};
        for (int i = 0; i < 4; i++) {
            bins[i] = new TrashBin(i * 150 + 100, 500, types[i], names[i]);
            add(bins[i]);
        }

        // Khởi tạo rác
        trash = new Trash();
        add(trash);

        // Nút thoát
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(largeFont); // Thiết lập font chữ lớn hơn
        exitButton.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        exitButton.setBounds(20, 540, 150, 50); // Điều chỉnh kích thước và vị trí
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);
updateBinsDisplay();
// Khởi chạy UDPClient trên luồng riêng để nhận điểm của đối thủ từ server
        Thread udpThread = new Thread(new UDPClient(this, serverAddress, port));
        udpThread.start();
// Bắt đầu trò chơi
        startGame();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
//  tat de sua trash

    public void startGame() {
        // Timer để điều khiển rơi của rác
        gameTimer = new Timer(50, new ActionListener() {
            private int timeElapsed = 0; // Biến để đếm thời gian
            @Override
            public void actionPerformed(ActionEvent e) {
                trash.moveDown(timeElapsed);
                if (trash.reachedBottom()) {
                    boolean correct = bins[currentBinIndex].isCorrectBin(trash);
                    updateScore(correct);
                    trash.resetPosition();
                }
            }
        });
        gameTimer.start();

        // Timer để giảm thời gian còn lại
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timeLabel.setText("Time: " + timeRemaining + "s");
                if (timeRemaining <= 0) {
                    countdownTimer.stop();
                    gameTimer.stop();
                    endGame();
                }
            }
        });
        countdownTimer.start();
    }

    public void endGame() {
        JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + currentPlayer.getScore());
    }


    public void updateScore(boolean correct) {
        if (correct) {
            currentPlayer.addScore(1);
        } else {
            currentPlayer.addScore(-1);
        }
        playerScoreLabel.setText(currentPlayer.getUsername() + ": " + currentPlayer.getScore());
    }

//     Hàm di chuyển thùng rác và thay đổi thứ tự
    public void moveBin1(int direction) {
        // Di chuyển sang trái hoặc phải
        bins[currentBinIndex].reset(); // Trở lại màu mặc định

        if (direction == -1) { // Sang trái
            currentBinIndex = (currentBinIndex - 1 + bins.length) % bins.length;
        } else if (direction == 1) { // Sang phải
            currentBinIndex = (currentBinIndex + 1) % bins.length;
        }

        // Đặt thùng hiện tại về giữa
        for (int i = 0; i < bins.length; i++) {
            bins[i].setBounds((i - currentBinIndex + 2) * 150 + 100, 500, 100, 100);
        }
        bins[currentBinIndex].rotate(); // Đánh dấu thùng rác mới được chọn
    }
// Phần moveBin đã được chỉnh sửa
    public void moveBin(int direction) {
        // Hoán đổi thứ tự thùng rác
        if (direction == -1) { // Sang trái
            TrashBin firstBin = bins[0]; // Lưu lại thùng rác đầu tiên
            // Dịch chuyển các thùng rác
            for (int i = 0; i < bins.length - 1; i++) {
                bins[i] = bins[i + 1];
            }
            bins[bins.length - 1] = firstBin; // Đưa thùng đầu tiên xuống cuối
        } else if (direction == 1) { // Sang phải
            TrashBin lastBin = bins[bins.length - 1]; // Lưu lại thùng rác cuối cùng
            // Dịch chuyển các thùng rác
            for (int i = bins.length - 1; i > 0; i--) {
                bins[i] = bins[i - 1];
            }
            bins[0] = lastBin; // Đưa thùng cuối cùng lên đầu
        }
//     Đặt currentBinIndex về vị trí giữa sau khi hoán đổi
    currentBinIndex = 1; // Đảm bảo thùng rác ở giữa (số 2) luôn là thùng được chọn
        // Cập nhật hiển thị sau khi hoán đổi
        updateBinsDisplay();
    }

    // Phương thức để cập nhật hiển thị của các thùng rác ở vị trí cố định
    public void updateBinsDisplay() {
        for (int i = 0; i < bins.length; i++) {
            // Đặt lại vị trí cho từng thùng rác nhưng giữ nguyên các vị trí cố định
//            bins[i].setBounds(i * 150 + 100, 500, 100, 100); // Giữ vị trí cố định
            bins[i].setBounds((i - currentBinIndex + 2) * 150 + 100, 500, 100, 100); // Cập nhật vị trí của các thùng rác
            bins[i].reset(); // Đặt lại màu mặc định
        }
        bins[1].rotate(); // Đánh dấu thùng rác mới được chọn
    }

    public void updateOpponentScore(int score) {
        opponentPlayer.setScore(score); // Cập nhật điểm đối thủ
        SwingUtilities.invokeLater(() -> {
            opponentScoreLabel.setText(opponentPlayer.getUsername() + ": " + opponentPlayer.getScore());
        });
    }



    public void createAndShowUI() {
        JFrame frame = new JFrame("Trash Sorting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addKeyListener(new GameController(this));
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }
}
