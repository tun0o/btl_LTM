

package client.GameUI;
import client.*;
import client.LeaderBoard_Rematch.MatchResult;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.AbstractBorder;


public class GameUI extends JPanel {
    private JLabel playerScoreLabel, opponentScoreLabel, timeLabel;
    private int playerScore = 0;
    private int opponentScore = 0;
    private int timeRemaining = 3;
    private TrashBin[] bins = new TrashBin[4];
    public int currentBinIndex = 1; // Thùng ở giữa (nhựa) bắt đầu là chỉ số 1
    private Trash trash;
    private Timer countdownTimer; // Khai báo biến ở cấp độ lớp
    private Timer gameTimer; // Khai báo biến ở cấp độ lớp
private  Client client;





    private Image background;
    private String currentPlayer; // Người chơi hiện tại
    private String opponentPlayer; // Đối thủ


    public GameUI(String currentPlayer, String opponentPlayer,Client client) {
        this.currentPlayer= currentPlayer;
        this.client = client;
        this.opponentPlayer= opponentPlayer;
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
        playerScoreLabel = new JLabel(currentPlayer+ ": " + playerScore);
        playerScoreLabel.setFont(largeFont); // Thiết lập font chữ lớn hơn
        playerScoreLabel.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        playerScoreLabel.setBounds(50, 20, 300, 50); // Điều chỉnh kích thước và vị trí
        add(playerScoreLabel);


        // Thông tin đối thủ
        opponentScoreLabel = new JLabel(opponentPlayer+ ": " + opponentScore);
        opponentScoreLabel.setFont(largeFont); // Thiết lập font chữ lớn hơn
        opponentScoreLabel.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        opponentScoreLabel.setBounds(650, 20, 300, 50); // Điều chỉnh kích thước và vị trí
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
//        JButton exitButton = new JButton("Exit");
//        exitButton.setFont(largeFont); // Thiết lập font chữ lớn hơn
//        exitButton.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
//        exitButton.setBounds(20, 540, 150, 50); // Điều chỉnh kích thước và vị trí
//        exitButton.addActionListener(e -> System.exit(0));
//        add(exitButton);
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(largeFont); // Thiết lập font chữ lớn hơn
        exitButton.setForeground(Color.WHITE); // Đổi màu chữ thành trắng
        exitButton.setBackground(Color.RED); // Đổi màu nền thành đỏ
        exitButton.setFocusPainted(false); // Xóa viền khi nhấn vào nút
        exitButton.setBounds(20, 540, 150, 50); // Điều chỉnh kích thước và vị trí

        // Tạo viền bo tròn
        exitButton.setBorder(new RoundedBorder(20)); // Bán kính bo tròn là 20

        exitButton.addActionListener(e -> endGame(currentPlayer+":surrender:"+opponentPlayer,true));
        add(exitButton);
        updateBinsDisplay();

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
                    endGame(currentPlayer,false);
                }
            }
        });
        countdownTimer.start();
    }
    public void endGame(String surrenderResult,boolean isSurrendered) {
        // Remove the current game interface and display the match result screen
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
if(isSurrendered) client.sendSurrender(surrenderResult);
        // Create and add the MatchResult screen
        frame.add(new MatchResult(currentPlayer, opponentPlayer, playerScore, opponentScore, client,surrenderResult,isSurrendered));

        // Revalidate and repaint the frame to reflect changes
        frame.revalidate();
        frame.repaint();
        client.sendMatchInfo(currentPlayer+','+opponentPlayer+','+playerScore+','+opponentScore);
    }
    public void updateScore1(boolean correct) {
        if (correct) {
            ++playerScore;
        } else {
            --playerScore;
        }
        client.sendScore(playerScore,this.currentPlayer,this.opponentPlayer);
        playerScoreLabel.setText(currentPlayer + ": " + playerScore);
    }
    public void updateScore(boolean correct) {
        if (correct) {
            ++playerScore;
            playerScoreLabel.setForeground(Color.GREEN); // Đổi màu thành xanh khi điểm tăng
        } else {
            --playerScore;
            playerScoreLabel.setForeground(Color.RED); // Đổi màu thành đỏ khi điểm giảm
        }
        client.sendScore(playerScore, this.currentPlayer, this.opponentPlayer);
        playerScoreLabel.setText(currentPlayer + ": " + playerScore);

        // Đặt lại màu trắng sau 500ms
        new Timer(1500, e -> playerScoreLabel.setForeground(Color.WHITE)).start();
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


    public void updateOpponentScore1(String score) {
        this.opponentScore =Integer.parseInt(score); // Cập nhật điểm đối thủ
        SwingUtilities.invokeLater(() -> {
            opponentScoreLabel.setText(opponentPlayer + ": " + this.opponentScore);
        });
    }
    public void updateOpponentScore(String score) {
        this.opponentScore = Integer.parseInt(score); // Cập nhật điểm đối thủ
        SwingUtilities.invokeLater(() -> {
            opponentScoreLabel.setText(opponentPlayer + ": " + this.opponentScore);

            // Đổi màu đối thủ khi điểm thay đổi
            if (Integer.parseInt(score) > opponentScore) {
                opponentScoreLabel.setForeground(Color.GREEN); // Nếu điểm tăng
            } else {
                opponentScoreLabel.setForeground(Color.RED); // Nếu điểm giảm
            }

            // Đặt lại màu trắng sau 500ms
            new Timer(1500, e -> opponentScoreLabel.setForeground(Color.WHITE)).start();
        });
    }



    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(c.getForeground());
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = this.radius;
            return insets;
        }
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
