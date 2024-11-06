import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard{
    // Cấu hình thông tin kết nối MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/database";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Leaderboard().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Bảng Xếp Hạng");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // Tiêu đề căn giữa
        JLabel titleLabel = new JLabel("Bảng Xếp Hạng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Kết nối và lấy dữ liệu từ DB
        List<String[]> playerData = getPlayerDataFromDB();

        // Tạo bảng với tên cột, thêm "Rank" vào columnNames
        String[] columnNames = {"Rank", "Player", "Wins", "Points"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Thêm dữ liệu vào bảng với cột "Rank" tự động đánh số
        int rank = 1;
        for (String[] row : playerData) {
            String[] rowWithRank = new String[row.length + 1];
            rowWithRank[0] = String.valueOf(rank++); // Thêm số thứ tự
            System.arraycopy(row, 0, rowWithRank, 1, row.length);
            model.addRow(rowWithRank);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Tạo một JPanel để thêm khoảng cách
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Thêm bảng vào giao diện
        frame.add(tablePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private List<String[]> getPlayerDataFromDB() {
        List<String[]> data = new ArrayList<>();

        // Truy vấn dữ liệu và sắp xếp theo Score để thể hiện xếp hạng
        String query = "SELECT username, noWin, rank_point FROM player ORDER BY rank_point DESC";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String player = resultSet.getString("username");
                String noWin = String.valueOf(resultSet.getInt("noWin"));
                String points = String.valueOf(resultSet.getInt("rank_point"));

                data.add(new String[]{player, noWin,  points});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}
