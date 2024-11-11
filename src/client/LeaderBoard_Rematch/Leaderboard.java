package client.LeaderBoard_Rematch;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private String allPlayers;

    // Constructor to accept allPlayers data
    public Leaderboard(String allPlayers) {
        this.allPlayers = allPlayers;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Bảng Xếp Hạng");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Tăng kích thước cửa sổ
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // Căn giữa cửa sổ trên màn hình
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Bảng Xếp Hạng", SwingConstants.CENTER);
        // Tăng kích thước font cho tiêu đề
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Lấy dữ liệu người chơi từ chuỗi allPlayers
        List<String[]> playerData = parsePlayerData(allPlayers);

        String[] columnNames = {"Xếp hạng", "Tên người chơi", "Số trận thắng", "Điểm xếp hạng"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        int rank = 1;
        for (String[] row : playerData) {
            String[] rowWithRank = new String[row.length + 1];
            rowWithRank[0] = String.valueOf(rank++);
            System.arraycopy(row, 0, rowWithRank, 1, row.length);
            model.addRow(rowWithRank);
        }

        JTable table = new JTable(model);

        // Căn giữa văn bản trong các ô của bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Tăng kích thước chữ trong bảng
        table.setFont(new Font("Serif", Font.PLAIN, 18));

        // Tăng chiều cao của các ô trong bảng
        table.setRowHeight(40); // Set chiều cao của các hàng trong bảng

        // Tăng kích thước chữ của các tiêu đề cột
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Serif", Font.BOLD, 20)); // Tăng kích thước font tiêu đề cột
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Tăng chiều cao của tiêu đề cột

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(tablePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private List<String[]> parsePlayerData(String allPlayers) {
        List<String[]> data = new ArrayList<>();
        String[] players = allPlayers.split(";");

        for (String player : players) {
            String[] info = player.split(":");
            if (info.length == 4) { // Bỏ qua ALLPLAYER
                String username = info[0];
                String noWin = info[2];
                String points = info[1];
                data.add(new String[]{username, noWin, points});
            }
        }

        // Sắp xếp theo điểm (descending order)
        data.sort((a, b) -> Integer.parseInt(b[2]) - Integer.parseInt(a[2]));
        return data;
    }
}
