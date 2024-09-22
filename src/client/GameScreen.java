package client;
import javax.swing.*;

public class GameScreen extends JFrame {
    private Client client;
    private JLabel scoreLabel;

    public GameScreen(Client client) {
        this.client = client;
        setTitle("Trò chơi");
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

        scoreLabel = new JLabel("Điểm: 0");
        scoreLabel.setBounds(10, 10, 100, 25);
        panel.add(scoreLabel);
    }
}
