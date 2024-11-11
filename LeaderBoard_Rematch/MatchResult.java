import javax.swing.*;

import client.Client;
import client.GameUI.GameUI;
import client.GameUI.HomeScreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class MatchResult extends JPanel {
    private JLabel player1NameLabel;
    private JLabel player2NameLabel;
    private JLabel scoreDifferenceLabel;
    private JLabel resultNotificationLabel;
    private JButton rematchButton;
    private JButton leaveRoomButton;
    private Client client;

    public MatchResult(String player1Name, String player2Name, int player1Score, int player2Score, Client client) {
        this.client = client;
        setLayout(null);
        setPreferredSize(new Dimension(600, 400));

        // Set up labels for player names
        player1NameLabel = new JLabel(player1Name);
        player1NameLabel.setOpaque(true);
        player1NameLabel.setBackground(Color.GREEN);
        player1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player1NameLabel.setBounds(30, 20, 150, 50);
        add(player1NameLabel);

        player2NameLabel = new JLabel(player2Name);
        player2NameLabel.setOpaque(true);
        player2NameLabel.setBackground(Color.GREEN);
        player2NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player2NameLabel.setBounds(420, 20, 150, 50);
        add(player2NameLabel);

        // Set up label for score difference
        scoreDifferenceLabel = new JLabel("Hiệu số: " + player1Score + " - " + player2Score);
        scoreDifferenceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreDifferenceLabel.setBounds(240, 20, 100, 50);
        add(scoreDifferenceLabel);

        // Set up result notification label
        String resultText = (player1Score > player2Score) ? player1Name + " thắng!" :
                (player1Score < player2Score) ? player2Name + " thắng!" : "Hòa!";
        resultNotificationLabel = new JLabel("Kết quả: " + resultText);
        resultNotificationLabel.setOpaque(true);
        resultNotificationLabel.setBackground(Color.CYAN);
        resultNotificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultNotificationLabel.setBounds(150, 100, 300, 50);
        add(resultNotificationLabel);

        // Set up rematch button
        rematchButton = new JButton("Đấu lại");
        rematchButton.setBackground(Color.YELLOW);
        rematchButton.setBounds(250, 200, 100, 50);
        rematchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showOptionDialog(null,
                        "Đối thủ của bạn muốn đấu lại. Bạn có muốn tham gia?",
                        "Xác nhận đấu lại",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Có", "Không"},
                        "Có");
        
                sendMatchResult(player1Name, player2Name, player1Score, player2Score);
        
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Trận đấu mới bắt đầu!");
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(MatchResult.this);
                    frame.getContentPane().removeAll();
                    frame.add(new GameUI(player1Name, player2Name, client));
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Đưa bạn về trang chủ...");
                    openHomeScreen();
                }
            }
        });
        add(rematchButton);

        // Set up leave room button
        leaveRoomButton = new JButton("Rời phòng");
        leaveRoomButton.setBackground(Color.RED);
        leaveRoomButton.setForeground(Color.WHITE);
        leaveRoomButton.setBounds(200, 300, 200, 50);
        leaveRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Rời phòng...");
                sendMatchResult(player1Name, player2Name, player1Score, player2Score);
                openHomeScreen();
            }
        });
        add(leaveRoomButton);
    }

    private void openHomeScreen() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.add(new HomeScreen(client.getUsername(), client.getOnlinePlayers(), client.getAllPlayers(), client));
        frame.revalidate();
        frame.repaint();
    }

    private void sendMatchResult(String player1Name, String player2Name, int player1Score, int player2Score) {
        try (Socket socket = new Socket("localhost", 12345)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String result = player1Score > player2Score ? "win" : (player1Score < player2Score ? "lose" : "draw");
            // Send the match result along with player names to the server
            out.println(player1Name + "," + player2Name + "," + result);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error sending result to the server.");
        }
    }

    public void createAndShowUI() {
        JFrame frame = new JFrame("Match Result");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


//Theêm vaào File GameUI.java vaà xoaá hamàm main file nayày

//public void endGame() {
//     JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//     frame.getContentPane().removeAll();
//     frame.add(new MatchResult(currentPlayer, opponentPlayer, playerScore, opponentScore, client));
//     frame.revalidate();
//     frame.repaint();
// }