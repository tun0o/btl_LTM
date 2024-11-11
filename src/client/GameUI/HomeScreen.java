package client.GameUI;

import client.Client;
import client.LeaderBoard_Rematch.Leaderboard;
import client.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class HomeScreen extends JFrame {
    private static String username;
    private int highScore;
    private JPanel playerListPanel;
    private List<Player> players;
    private static String onlinePlayers;
    private static String allPlayers;
    public Client client;

    public HomeScreen(String username, String onlinePlayers, String allPlayers, Client client) {
        this.username = username;
        this.client = client;
        this.onlinePlayers = onlinePlayers;
        this.allPlayers = allPlayers;
        this.highScore = 123;
        client.setHomeScreen(this);

        setTitle("Trang chủ");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon("resources/background.png");
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setOpaque(false);
        add(backgroundPanel);

        // Panel for the player's information and avatar on the top-left (Vertical layout)
        JPanel currentUserPanel = new JPanel();
        currentUserPanel.setLayout(new BoxLayout(currentUserPanel, BoxLayout.Y_AXIS));  // Change to Y_AXIS for vertical layout
        currentUserPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        currentUserPanel.setOpaque(false);

// Load and scale the avatar icon
        ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/resources/img_8.png"));
        Image scaledImage = avatarIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel avatarLabel = new JLabel(new ImageIcon(scaledImage));
        currentUserPanel.add(avatarLabel);

// Retrieve player info
        Map<String, String[]> playerInfo = parseAllPlayers(allPlayers);
        String[] currentPlayerData = playerInfo.getOrDefault(username, new String[]{"0", "0", "0"});

        int rankScore = Integer.parseInt(currentPlayerData[0]);
        int wins = Integer.parseInt(currentPlayerData[1]);
        int points = Integer.parseInt(currentPlayerData[2]);

// Display player information
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        JLabel userInfoLabel = new JLabel(
                "<html><div style='text-align: left;'><b>" + username + "</b><br>" +
//                        "Kỷ lục: " + highScore + "<br>" +
                        "Điểm xếp hạng: " + rankScore + "<br>" +
                        "Số trận thắng: " + wins + "<br>"
//                        +"Điểm số: " + points + "</div></html>"
        );
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userInfoLabel.setForeground(Color.BLACK);
        userInfoPanel.add(userInfoLabel);

// Add player info panel to the current user panel
        currentUserPanel.add(userInfoPanel);

// Panel for buttons (Find Match and Leaderboard) directly below player info
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Layout set to Y_AXIS for vertical arrangement
        buttonPanel.setOpaque(false);

        JButton randomMatchButton = new JButton("Ghép ngẫu nhiên");
        JButton leaderboardButton = new JButton("Bảng xếp hạng");

        Dimension buttonSize = new Dimension(180, 30); // Defining a common size

        randomMatchButton.setPreferredSize(buttonSize);
        leaderboardButton.setPreferredSize(buttonSize);

// Make sure the buttons do not resize by setting the maximum and minimum size to be the same as the preferred size
        randomMatchButton.setMaximumSize(buttonSize);
        randomMatchButton.setMinimumSize(buttonSize);
        leaderboardButton.setMaximumSize(buttonSize);
        leaderboardButton.setMinimumSize(buttonSize);
        randomMatchButton.setFont(new Font("Arial", Font.BOLD, 16));
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, 16));
        randomMatchButton.setBackground(Color.YELLOW);
        leaderboardButton.setBackground(Color.YELLOW);

// Add buttons vertically
        buttonPanel.add(Box.createVerticalStrut(10)); // Added vertical spacing before buttons
        buttonPanel.add(randomMatchButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // Added vertical spacing after randomMatchButton
        buttonPanel.add(leaderboardButton);


// Add button panel directly below player info (on the left side)
        currentUserPanel.add(buttonPanel);

// Add currentUserPanel to top-left panel
        JPanel topLeftPanel = new JPanel(new BorderLayout());
        topLeftPanel.setOpaque(false);
        topLeftPanel.add(currentUserPanel, BorderLayout.NORTH);
        backgroundPanel.add(topLeftPanel, BorderLayout.WEST);

        // Panel to display the list of players on the right side
        playerListPanel = new JPanel();
        playerListPanel.setLayout(new GridBagLayout());
        playerListPanel.setPreferredSize(new Dimension(100, 200));  // Smaller size for player list panel
        JPanel playerListContainer = new JPanel(new BorderLayout());

// Reduced header panel height
        JPanel headerPanel = new JPanel(new GridLayout(1, 3));
        // Header row with better design
        headerPanel.setPreferredSize(new Dimension(100, 30));  // Adjusted height
        headerPanel.setBackground(Color.LIGHT_GRAY);
//        headerPanel.add(new JLabel("Người chơi", SwingConstants.CENTER));
//        headerPanel.add(new JLabel("Trạng thái", SwingConstants.CENTER));
        headerPanel.add(new JLabel("", SwingConstants.CENTER));

        headerPanel.setBackground(Color.LIGHT_GRAY);
        playerListPanel.setBackground(Color.WHITE);

        playerListContainer.add(headerPanel, BorderLayout.NORTH);
        playerListContainer.add(new JScrollPane(playerListPanel), BorderLayout.CENTER);

// Adjust the overall player list container size
        playerListContainer.setPreferredSize(new Dimension(100, 250));  // Reduced overall container size

// Adding player list container to the background panel
        backgroundPanel.add(playerListContainer, BorderLayout.CENTER);



        // Button event listeners
        randomMatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomMatchButton.setText("Đang tìm trận");  // Change button text
                client.findRandomMatch();
            }
        });

        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLeaderboard();
            }
        });

        setVisible(true);
    }

    public void showMatchInviteDialog(String type, String inviter) {
        int response = JOptionPane.NO_OPTION;
        if (type.equals("random")) {
            response = JOptionPane.showOptionDialog(
                    this,
                    " Tìm thấy trận.",
                    "Match Found",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Chơi ngay", "Từ chối"},
                    "Chơi ngay"
            );
        }else{
            response = JOptionPane.showOptionDialog(
                    this,
                    inviter + " mời bạn chơi.",
                    "Match Found",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Chơi ngay", "Từ chối"},
                    "Chơi ngay"
            );
        }

        if (response == JOptionPane.YES_OPTION) {
            client.accept(this.username);  // Example method to send acceptance
        } else {
            client.decline(this.username); // Send decline
        }
    }


    public void loadPlayerList2(String onlinePlayers, String allPlayers) {
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

        // Phân tích onlinePlayers
        Set<String> onlineSet = new HashSet<>(Arrays.asList(onlinePlayers.split(",")));
        // Phân tích allPlayers
        String[] allPlayersArray = allPlayers.split(";");
        int row = 1;
        for (String playerData : allPlayersArray) {
            String[] playerInfo = playerData.split(":");
            if (playerInfo.length < 4) continue; // Đảm bảo có đủ thông tin
            String playerUsername = playerInfo[0];
            if(playerUsername.equals(username)) continue;
            String status = onlineSet.contains(playerUsername) ? "Online" : "Offline";
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel nameLabel = new JLabel(playerUsername);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            playerListPanel.add(nameLabel, gbc);
            gbc.gridx = 1;
            JLabel statusLabel = new JLabel(status);
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            playerListPanel.add(statusLabel, gbc);
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
    public void loadPlayerList1(String onlinePlayers, String allPlayers) {
        // Cấu hình bảng
        String[] columnNames = {"Người chơi", "Trạng thái", "Hành động"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Phân tích onlinePlayers
        Set<String> onlineSet = new HashSet<>(Arrays.asList(onlinePlayers.split(",")));

        // Phân tích allPlayers
        String[] allPlayersArray = allPlayers.split(";");

        // Lặp qua tất cả người chơi
        for (String playerData : allPlayersArray) {
            String[] playerInfo = playerData.split(":");
            if (playerInfo.length < 4) continue; // Đảm bảo có đủ thông tin

            String playerUsername = playerInfo[0];
            if (playerUsername.equals(username)) continue; // Không hiển thị người chơi hiện tại

            String status = onlineSet.contains(playerUsername) ? "Online" : "Offline";

            // Thêm dòng mới vào bảng
            Object[] row = {playerUsername, status, createActionButton(status, playerUsername)};
            model.addRow(row);
        }

        // Tạo bảng và đặt model cho bảng
        JTable playerTable = new JTable(model);
        playerTable.setFillsViewportHeight(true);  // Đảm bảo bảng chiếm hết chiều cao của vùng hiển thị

        // Tùy chỉnh giao diện của bảng
        playerTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Cột Người chơi
        playerTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Cột Trạng thái
        playerTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Cột Hành động

        // Tạo một JScrollPane để bảng có thể cuộn
        JScrollPane scrollPane = new JScrollPane(playerTable);
        playerListPanel.removeAll();  // Xóa tất cả các thành phần cũ trong playerListPanel
        playerListPanel.add(scrollPane, BorderLayout.CENTER); // Thêm bảng vào panel
        playerListPanel.revalidate();
        playerListPanel.repaint();
    }
    public void loadPlayerList(String onlinePlayers, String allPlayers) {
        playerListPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 15, 5, 15); // Add more space between columns

        // Header row with larger font and styling
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel headerName = new JLabel("Người chơi");
        headerName.setFont(new Font("Arial", Font.BOLD, 16));
        headerName.setForeground(Color.BLACK);
        headerName.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        playerListPanel.add(headerName, gbc);

        gbc.gridx = 1;
        JLabel headerStatus = new JLabel("Trạng thái");
        headerStatus.setFont(new Font("Arial", Font.BOLD, 16));
        headerStatus.setForeground(Color.BLACK);
        headerStatus.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        playerListPanel.add(headerStatus, gbc);

        gbc.gridx = 2;
        JLabel headerAction = new JLabel("");
        playerListPanel.add(headerAction, gbc);

        // Parse onlinePlayers
        Set<String> onlineSet = new HashSet<>(Arrays.asList(onlinePlayers.split(",")));

        // Parse allPlayers
        String[] allPlayersArray = allPlayers.split(";");
        int row = 1;

        for (String playerData : allPlayersArray) {
            String[] playerInfo = playerData.split(":");
            if (playerInfo.length < 4) continue; // Ensure there is enough info

            String playerUsername = playerInfo[0];
            if (playerUsername.equals(username)) continue;

            String status = onlineSet.contains(playerUsername) ? "Online" : "Offline";

            // Name Column - Centered
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel nameLabel = new JLabel(playerUsername);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
            playerListPanel.add(nameLabel, gbc);

            // Status Column with color - Centered
            gbc.gridx = 1;
            JLabel statusLabel = new JLabel(status);
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
            if ("Online".equals(status)) {
                statusLabel.setForeground(Color.GREEN);  // Green for online
            } else {
                statusLabel.setForeground(Color.RED);  // Red for offline
            }
            playerListPanel.add(statusLabel, gbc);

            // Action Column (Invite Button)
            gbc.gridx = 2;
            if (status.equals("Online")) {
                JButton inviteButton = new JButton("Mời");
                inviteButton.setFont(new Font("Arial", Font.PLAIN, 12));
                inviteButton.setPreferredSize(new Dimension(60, 30));
                inviteButton.addActionListener(e -> invitePlayer(playerUsername));
                playerListPanel.add(inviteButton, gbc);
            } else {
                playerListPanel.add(new JLabel(""), gbc);  // No "Invite" button for offline players
            }
            row++;
        }

        playerListPanel.revalidate();
        playerListPanel.repaint();
    }


    private JButton createActionButton(String status, String playerUsername) {
        JButton inviteButton = new JButton("Mời");
        inviteButton.setFont(new Font("Arial", Font.PLAIN, 12));
        inviteButton.setPreferredSize(new Dimension(60, 30));
        inviteButton.setEnabled(status.equals("Online")); // Chỉ bật nút nếu người chơi "Online"

        inviteButton.addActionListener(e -> invitePlayer(playerUsername));  // Gửi lời mời khi nhấn
        return inviteButton;
    }
    private void openLeaderboard() {
//        JOptionPane.showMessageDialog(this, "Chuyển đến trang bảng xếp hạng...");
        new Leaderboard(allPlayers);  // Pass
    }

    private void invitePlayer(String username) {
        client.sendInvite(username);
        client.accept(this.username);
        JOptionPane.showMessageDialog(this, "Đã gửi lời mời đến " + username);
    }

    private Map<String, String[]> parseAllPlayers(String allPlayers) {
        Map<String, String[]> playerInfo = new HashMap<>();
        String[] players = allPlayers.split(";");
        for (String player : players) {
            String[] details = player.split(":");
            if (details.length == 4) {
                String[] info = {details[1], details[2], details[3]};
                playerInfo.put(details[0], info);
            }
        }
        return playerInfo;
    }

    public static void main(String[] args) {
//        new HomeScreen("player1", "player1,player2", "player1:0:1:100;player2:5:3:150", new Client());
    }
}
