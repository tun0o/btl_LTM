package client.GameUI;

import client.Client;
import client.Player;
import database.PlayerDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class HomeScreen extends JFrame {
    private static String username;    // Tên người chơi hiện tại
    private int highScore;      // Kỷ lục của người chơi hiện tại
    private JPanel playerListPanel;

    public HomeScreen(String username, int highScore, List<Player> players) {
        this.username = username;
        this.highScore = highScore;
        setTitle("Trang chủ");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel thông tin người chơi hiện tại
        JPanel currentUserPanel = new JPanel();
        currentUserPanel.setLayout(new BoxLayout(currentUserPanel, BoxLayout.Y_AXIS));
        JLabel userInfoLabel = new JLabel("<html><div style='text-align: center;'><b>" + username + "</b><br>Kỷ lục: " + highScore + "</div></html>");
        userInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentUserPanel.add(userInfoLabel);
        currentUserPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Nút ghép ngẫu nhiên và bảng xếp hạng
        JButton randomMatchButton = new JButton("Ghép ngẫu nhiên");
        JButton leaderboardButton = new JButton("Bảng xếp hạng");
        randomMatchButton.setPreferredSize(new Dimension(200, 50));
        leaderboardButton.setPreferredSize(new Dimension(200, 50));
        randomMatchButton.setBackground(Color.YELLOW);
        leaderboardButton.setBackground(Color.YELLOW);
        randomMatchButton.setFont(new Font("Arial", Font.BOLD, 14));
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, 14));

        currentUserPanel.add(randomMatchButton);
        currentUserPanel.add(Box.createRigidArea(new Dimension(0, 10)));  // Khoảng cách giữa các nút
        currentUserPanel.add(leaderboardButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);  // Khoảng cách
        add(currentUserPanel, gbc);

        // Panel danh sách người chơi
        playerListPanel = new JPanel();
        playerListPanel.setLayout(new GridBagLayout());
        GridBagConstraints playerGbc = new GridBagConstraints();
        playerGbc.fill = GridBagConstraints.HORIZONTAL;
        playerGbc.insets = new Insets(5, 5, 5, 5);

        loadPlayerList(players);

        JScrollPane scrollPane = new JScrollPane(playerListPanel);
        scrollPane.setPreferredSize(new Dimension(400, 500));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(scrollPane, gbc);

        // Sự kiện cho các nút
        randomMatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gọi hàm tìm trận ngẫu nhiên
                startRandomMatch();
            }
        });

        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Điều hướng đến trang bảng xếp hạng
                openLeaderboard();
            }
        });

        setVisible(true);
    }

    // Hiển thị danh sách người chơi
    private void loadPlayerList(List<Player> players) {
        playerListPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Header row
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel headerName = new JLabel("Người chơi");
        headerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerListPanel.add(headerName, gbc);

        gbc.gridx = 1;
        JLabel headerStatus = new JLabel("Trạng thái");
        headerStatus.setFont(new Font("Arial", Font.BOLD, 14));
        playerListPanel.add(headerStatus, gbc);

        gbc.gridx = 2;
        JLabel headerAction = new JLabel("");
        playerListPanel.add(headerAction, gbc);

        int row = 1;
        for (Player player : players) {
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel nameLabel = new JLabel(player.getUsername());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            playerListPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            JLabel statusLabel = new JLabel(player.getStatus());
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            playerListPanel.add(statusLabel, gbc);

            gbc.gridx = 2;
            JButton inviteButton = new JButton("Mời");
            inviteButton.setFont(new Font("Arial", Font.PLAIN, 12));
            inviteButton.setPreferredSize(new Dimension(60, 30));
            inviteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    invitePlayer(player.getUsername());
                }
            });
            playerListPanel.add(inviteButton, gbc);

            row++;
        }
        playerListPanel.revalidate();
        playerListPanel.repaint();
    }

    private void startRandomMatch() {
        // Logic tìm trận ngẫu nhiên
        Client client = new Client();
        client.sendMessage("RANDOM_MATCH");
        JOptionPane.showMessageDialog(this, "Tìm trận ngẫu nhiên...");
    }

    private void openLeaderboard() {
        // Điều hướng đến trang bảng xếp hạng
        JOptionPane.showMessageDialog(this, "Chuyển đến trang bảng xếp hạng...");
    }

    private void invitePlayer(String username) {
        // Gửi lời mời chơi đến người chơi khác
        Client client = new Client();
        client.sendMessage("INVITE_TO_PLAY;"+username);
        JOptionPane.showMessageDialog(this, "Đã gửi lời mời đến " + username);
    }

    public static void main(String[] args) throws SQLException {
        // Ví dụ dữ liệu truy vấn từ database
        List<Player> players = new PlayerDAO().getAllPlayers(username);

        new HomeScreen("Người chơi 1", 100, players);
    }
}
