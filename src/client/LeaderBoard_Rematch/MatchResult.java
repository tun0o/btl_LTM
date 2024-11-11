package client.LeaderBoard_Rematch;

import javax.swing.*;
import client.Client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MatchResult extends JPanel {
    private JLabel player1NameLabel;
    private JLabel player2NameLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel resultNotificationLabel;
    private JButton rematchButton;
    private JButton leaveRoomButton;
    private Client client;
    public static String player1Name;
    public static String player2Name;

    public MatchResult(String player1Name, String player2Name, int player1Score, int player2Score, Client client, String surrenderResult, Boolean isSurrendered) {
        this.client = client;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        client.setMatchResult(this);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setPreferredSize(new Dimension(600, 400));

        // Set up a large font for all components
        Font largeFont = new Font("Arial", Font.BOLD, 24);

        // Player 1 Name Label
        player1NameLabel = new JLabel(player1Name);
        player1NameLabel.setOpaque(true);
        player1NameLabel.setBackground(Color.GREEN);
        player1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player1NameLabel.setFont(largeFont);
        player1NameLabel.setPreferredSize(new Dimension(200, 70));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(player1NameLabel, gbc);

        // Player 2 Name Label
        player2NameLabel = new JLabel(player2Name);
        player2NameLabel.setOpaque(true);
        player2NameLabel.setBackground(Color.GREEN);
        player2NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player2NameLabel.setFont(largeFont);
        player2NameLabel.setPreferredSize(new Dimension(200, 70));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 100, 0, 0); // Adding space between player1 and player2
        add(player2NameLabel, gbc);

        // Player 1 Score Label
        player1ScoreLabel = new JLabel(String.valueOf(player1Score));
        player1ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player1ScoreLabel.setFont(largeFont);
        player1ScoreLabel.setPreferredSize(new Dimension(200, 70));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0); // Reset insets
        add(player1ScoreLabel, gbc);

        // Player 2 Score Label
        player2ScoreLabel = new JLabel(String.valueOf(player2Score));
        player2ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player2ScoreLabel.setFont(largeFont);
        player2ScoreLabel.setPreferredSize(new Dimension(200, 70));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 100, 0, 0); // Adding space between player1 and player2 score
        add(player2ScoreLabel, gbc);

        // Result Notification Label
        String resultText;
        if (isSurrendered) {
            String[] parts = surrenderResult.split(":");
            String surrenderingPlayer = parts[0];
            resultText = surrenderingPlayer.equals(player1Name) ? player2Name + " thắng!" : player1Name + " thắng!";
        } else {
            resultText = (player1Score > player2Score) ? player1Name + " thắng!" :
                    (player1Score < player2Score) ? player2Name + " thắng!" : "Hòa!";
        }
        resultNotificationLabel = new JLabel("Kết quả: " + resultText);
        resultNotificationLabel.setOpaque(true);
        resultNotificationLabel.setBackground(Color.CYAN);
        resultNotificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultNotificationLabel.setFont(new Font("Arial", Font.BOLD, 28));
        resultNotificationLabel.setPreferredSize(new Dimension(500, 70));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0); // Added top padding to the result label
        add(resultNotificationLabel, gbc);

        // Rematch Button
        rematchButton = new JButton("Đấu lại");
        rematchButton.setBackground(Color.YELLOW);
        rematchButton.setFont(new Font("Arial", Font.BOLD, 18));
        rematchButton.setPreferredSize(new Dimension(300, 50));
        rematchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendRematch(player2Name);
                client.accept(player1Name);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0); // Added top padding to the rematch button
        add(rematchButton, gbc);

        // Leave Room Button
        leaveRoomButton = new JButton("Trang chủ");
        leaveRoomButton.setBackground(Color.RED);
        leaveRoomButton.setForeground(Color.WHITE);
        leaveRoomButton.setFont(new Font("Arial", Font.BOLD, 18));
        leaveRoomButton.setPreferredSize(new Dimension(300, 50));
        leaveRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openHomeScreen();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0); // Added top padding to the leave button
        add(leaveRoomButton, gbc);
    }

    // Method to show rematch confirmation dialog
    public void showRematchConfirmation() {
        int response = JOptionPane.showOptionDialog(
                null,
                "Đối thủ của bạn muốn đấu lại. Bạn có muốn tham gia?",
                "Xác nhận đấu lại",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Có", "Không"},
                "Có"
        );

        if (response == JOptionPane.YES_OPTION) {
            client.accept(player1Name);
        } else {
            client.decline(player1Name);
        }
    }

    private void openHomeScreen() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        Container container = frame.getContentPane();
        container.removeAll();
        container.setLayout(new CardLayout());
        container.revalidate();
        container.repaint();
        frame.dispose();
        client.handleLoginSuccess();
    }

    public void createAndShowUI() {
        JFrame frame = new JFrame("Match Result");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
