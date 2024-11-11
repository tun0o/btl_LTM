import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Leaderboard().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Bảng Xếp Hạng");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Bảng Xếp Hạng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Nhận chuỗi allPlayers từ server
        String allPlayers = getAllPlayersFromServer();
        
        // Lấy dữ liệu người chơi từ chuỗi allPlayers
        List<String[]> playerData = parsePlayerData(allPlayers);

        String[] columnNames = {"Rank", "Player", "Wins", "Points"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        int rank = 1;
        for (String[] row : playerData) {
            String[] rowWithRank = new String[row.length + 1];
            rowWithRank[0] = String.valueOf(rank++);
            System.arraycopy(row, 0, rowWithRank, 1, row.length);
            model.addRow(rowWithRank);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(tablePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private String getAllPlayersFromServer() {
        // Thực hiện nhận chuỗi từ server
        // Ví dụ:
        // return serverConnection.receiveAllPlayersData();
        return "ALLPLAYER:1:10:5:100;user1:10:5:100;user10:55:30:550;user2:20:10:200;user3:15:7:150;user4:30:15:300;user5:25:12:250;user6:40:20:400;user7:35:18:350;user8:50:25:500;user9:45:22:450;";
    }

    private List<String[]> parsePlayerData(String allPlayers) {
        List<String[]> data = new ArrayList<>();
        String[] players = allPlayers.split(";");

        for (String player : players) {
            String[] info = player.split(":");
            if (info.length == 4) { // Bỏ qua ALLPLAYER
                String username = info[0];
                String noWin = info[1];
                String points = info[3];
                data.add(new String[]{username, noWin, points});
            }
        }

        data.sort((a, b) -> Integer.parseInt(b[2]) - Integer.parseInt(a[2]));
        return data;
    }
}
