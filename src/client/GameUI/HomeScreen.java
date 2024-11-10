package client.GameUI;

import client.Client;
import client.Client1;
import client.Player;
import database.PlayerDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeScreen extends JFrame {
    private static String username;    // Tên người chơi hiện tại
    private int highScore;      // Kỷ lục của người chơi hiện tại
    private JPanel playerListPanel;
    private List<Player> players;
    private static String onlinePlayers;
    private static String allPlayers;
    public Client client;

    public HomeScreen(String username, String onlinePlayers,String allPlayers,Client client) {
        this.username = username;
        this.client = client;
        this.onlinePlayers =onlinePlayers;
        this.allPlayers = allPlayers;
        this.highScore = 123;
        client.setHomeScreen(this);

        setTitle("Trang chủ");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel backgroundPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                ImageIcon backroundIcon = new ImageIcon("resources/background.png");
                g.drawImage(backroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setOpaque(false);
        add(backgroundPanel);

        // Panel thông tin người chơi hiện tại
        JPanel currentUserPanel = new JPanel();
        currentUserPanel.setLayout(new BoxLayout(currentUserPanel, BoxLayout.Y_AXIS));
        currentUserPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel userInfoLabel = new JLabel("<html><div style='text-align: center;'><b>" + username + "</b><br>Kỷ lục: " + highScore + "</div></html>");
        userInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userInfoLabel.setForeground(Color.BLACK);
        currentUserPanel.add(userInfoLabel);
        currentUserPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Nút ghép ngẫu nhiên và bảng xếp hạng
        JButton randomMatchButton = new JButton("Ghép ngẫu nhiên");
        JButton leaderboardButton = new JButton("Bảng xếp hạng");

        randomMatchButton.setPreferredSize(new Dimension(220, 60));
        leaderboardButton.setPreferredSize(new Dimension(220, 60));
        randomMatchButton.setFont(new Font("Arial", Font.BOLD, 16));
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, 16));
        randomMatchButton.setBackground(Color.YELLOW);
        leaderboardButton.setBackground(Color.YELLOW);

        currentUserPanel.add(randomMatchButton);
        currentUserPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        currentUserPanel.add(leaderboardButton);

        add(currentUserPanel,BorderLayout.WEST);

        // Panel danh sách người chơi
        playerListPanel = new JPanel();
        playerListPanel.setLayout(new GridBagLayout());
        JPanel playerListContainer = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(1, 3));
        JLabel headerName = new JLabel("Người chơi", SwingConstants.CENTER);
        headerName.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel headerStatus = new JLabel("Trạng thái", SwingConstants.CENTER);
        headerStatus.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel headerAction = new JLabel("", SwingConstants.CENTER);

        headerPanel.add(headerName);
        headerPanel.add(headerStatus);
        headerPanel.add(headerAction);

        playerListContainer.add(headerPanel, BorderLayout.NORTH);
        playerListContainer.add(new JScrollPane(playerListPanel), BorderLayout.CENTER);

        loadPlayerList(this.onlinePlayers, this.allPlayers);

        add(playerListContainer, BorderLayout.CENTER);

        // Sự kiện cho các nút
        randomMatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gọi hàm tìm trận ngẫu nhiên
                client.findRandomMatch();
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

    public void showMatchInviteDialog(String type,String inviter) {
        int response = JOptionPane.NO_OPTION;
//        SwingUtilities.invokeLater(() -> {
            if (type.equals("random")) {
                response = JOptionPane.showOptionDialog(
                        this,
                        inviter + " has invited you to a match.",
                        "Match Found",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Accept", "Decline"},
                        "Accept"
                );
            }


            if (response == JOptionPane.YES_OPTION) {
                client.accept(this.username);  // Example method to send acceptance
            } else {
                client.decline(this.username); // Send decline
            }
//        });
    }
    // Hiển thị danh sách người chơi
    private void loadPlayerList1(List<Player> players) {
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
    // Hiển thị danh sách người chơi
    private void loadPlayerList(String onlinePlayers, String allPlayers) {
        playerListPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Header row
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        JLabel headerName = new JLabel("Người chơi");
//        headerName.setFont(new Font("Arial", Font.BOLD, 14));
//        playerListPanel.add(headerName, gbc);
//
//        gbc.gridx = 1;
//        JLabel headerStatus = new JLabel("Trạng thái");
//        headerStatus.setFont(new Font("Arial", Font.BOLD, 14));
//        playerListPanel.add(headerStatus, gbc);
//
//        gbc.gridx = 2;
//        JLabel headerAction = new JLabel("");
//        playerListPanel.add(headerAction, gbc);

        // Phân tích onlinePlayers
        Set<String> onlineSet = new HashSet<>(Arrays.asList(onlinePlayers.split(",")));

        // Phân tích allPlayers
        String[] allPlayersArray = allPlayers.split(";");
        int row = 0;
        for (String playerData : allPlayersArray) {
            String[] playerInfo = playerData.split(":");
            if (playerInfo.length < 4) continue; // Đảm bảo có đủ thông tin

            String playerUsername = playerInfo[0];
            String status = onlineSet.contains(playerUsername) ? "Online" : "Offline";

            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel nameLabel = new JLabel(playerUsername);
            //nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            playerListPanel.add(new JLabel(playerUsername, SwingConstants.CENTER), gbc);

            gbc.gridx = 1;
            //JLabel statusLabel = new JLabel(status);
            //statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            playerListPanel.add(new JLabel(status, SwingConstants.CENTER), gbc);

            gbc.gridx = 2;
            if (status.equals("Online")) {
                JButton inviteButton = new JButton("Mời");
                inviteButton.setFont(new Font("Arial", Font.PLAIN, 12));
                inviteButton.setPreferredSize(new Dimension(60, 30));
                inviteButton.addActionListener(e -> invitePlayer(playerUsername));
                playerListPanel.add(inviteButton, gbc);
            } else {
                playerListPanel.add(new JLabel(""), gbc); // Không có nút "Mời" cho người chơi offline
            }

            row++;
        }
        playerListPanel.revalidate();
        playerListPanel.repaint();
    }
    private void startRandomMatch() {
        // Logic tìm trận ngẫu nhiên
        Client1 client = new Client1();
        client.sendMessage("RANDOM_MATCH");
        JOptionPane.showMessageDialog(this, "Tìm trận ngẫu nhiên...");
    }

    private void openLeaderboard() {
        // Điều hướng đến trang bảng xếp hạng
        JOptionPane.showMessageDialog(this, "Chuyển đến trang bảng xếp hạng...");
    }

    private void invitePlayer(String username) {

        client.sendInvite(username);
        client.accept(this.username);
        JOptionPane.showMessageDialog(this, "Đã gửi lời mời đến " + username);
    }

    public static void main(String[] args) throws SQLException {
        String username  = "Người chơi 1";
//        new HomeScreen(username, "player1,player2","player1:info;player2:info", new Client("localhost", 12345));
    }
}
