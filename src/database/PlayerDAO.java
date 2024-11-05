package database;
import client.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    public boolean login(String username, String password) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM player WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public boolean register(String username, String password) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO player (username, password, rank_point, noWin, score, status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);

        // Set default values for rank_point, noWin, score, and status
        int defaultRankPoint = 0;
        int defaultNoWin = 0;
        int defaultScore = 0;
        String defaultStatus = "Online"; // or whatever the default status should be

        stmt.setInt(3, defaultRankPoint);
        stmt.setInt(4, defaultNoWin);
        stmt.setInt(5, defaultScore);
        stmt.setString(6, defaultStatus);

        int rowsInserted = stmt.executeUpdate();
        return rowsInserted > 0;
    }


    public List<Player> getAllPlayers() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        List<Player> players = new ArrayList<>();
        // Cập nhật truy vấn để lấy thêm thông tin
        String sql = "SELECT * FROM player ";

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String playerUsername = rs.getString("username");
            String playerPassword = rs.getString("password"); // Nếu cần thiết
            int rankPoint = rs.getInt("rank_point");
            int noWin = rs.getInt("noWin");
            int score = rs.getInt("score");

            // Tạo đối tượng Player mới với tất cả thuộc tính
            players.add(new Player(playerUsername, playerPassword, rankPoint, noWin, score));
        }

        System.out.println(players);
        return players;
    }

}

