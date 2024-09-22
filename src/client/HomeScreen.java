package client;
import javax.swing.*;

public class HomeScreen extends JFrame {
    private Client client;
    private JTextArea playerListArea;
    private JButton startGameButton;
    private JButton viewLeaderboardButton;

    public HomeScreen(Client client) {
        this.client = client;
        setTitle("Trang chủ");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        playerListArea = new JTextArea();
        playerListArea.setBounds(10, 10, 200, 200);
        panel.add(playerListArea);

        startGameButton = new JButton("Bắt đầu chơi");
        startGameButton.setBounds(220, 10, 150, 25);
        panel.add(startGameButton);

        viewLeaderboardButton = new JButton("Xem bảng xếp hạng");
        viewLeaderboardButton.setBounds(220, 50, 150, 25);
        panel.add(viewLeaderboardButton);
    }
}
