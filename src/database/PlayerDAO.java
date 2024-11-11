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
        String sql = "INSERT INTO player (username, password, rank_point, noWin) VALUES (?, ?, ?, ?)";
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
//        stmt.setInt(5, defaultScore);
//        stmt.setString(6, defaultStatus);

        int rowsInserted = stmt.executeUpdate();
        return rowsInserted > 0;
    }

    public void updatePlayerScores(String resultData) throws  SQLException{
        Connection connection = DatabaseConnection.getConnection();
        String[] data = resultData.split(",");
        String currentPlayer = data[0];
        String opponentPlayer = data[1];
        int playerScore = Integer.parseInt(data[2]);
        int opponentScore = Integer.parseInt(data[3]);

        int currentPlayerRankPoint = getPlayerRankPoint(currentPlayer);
        int opponentPlayerRankPoint = getPlayerRankPoint(opponentPlayer);

        int currentPlayerNewRankPoint = currentPlayerRankPoint;
        int opponentPlayerNewRankPoint = opponentPlayerRankPoint;

        // Update rank points based on the score comparison
        if (playerScore > opponentScore) {
            currentPlayerNewRankPoint += 2;  // Winner gets 2 points
        } else if (playerScore < opponentScore) {
            opponentPlayerNewRankPoint += 2;  // Winner gets 2 points
        } else {
            currentPlayerNewRankPoint += 1;  // Draw, both get 1 point
            opponentPlayerNewRankPoint += 1;
        }

        // Update both players' rank points
        updatePlayerRankPoint(currentPlayer, currentPlayerNewRankPoint);
        updatePlayerRankPoint(opponentPlayer, opponentPlayerNewRankPoint);
    }

    private int getPlayerRankPoint(String username) throws  SQLException{
        Connection connection = DatabaseConnection.getConnection();
        int rankPoint = 0;
        String query = "SELECT rank_point FROM player WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rankPoint = rs.getInt("rank_point");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rankPoint;
    }

    private void updatePlayerRankPoint(String username, int newRankPoint) throws  SQLException{
        Connection connection = DatabaseConnection.getConnection();
        String updateQuery = "UPDATE player SET rank_point = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setInt(1, newRankPoint);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            int score =0;

            // Tạo đối tượng Player mới với tất cả thuộc tính
            players.add(new Player(playerUsername, playerPassword, rankPoint, noWin, score));
        }

        System.out.println(players);
        return players;
    }
    public void addMatch(String matchInfo)  throws SQLException{
        Connection connection= DatabaseConnection.getConnection();
        String[] info = matchInfo.split(",");

        // Ensure the input string has exactly four values
        if (info.length != 4) {
            System.out.println("Invalid match information format.");

        }

        String player1Name = info[0].trim();
        String player2Name = info[1].trim();
        int player1Score = Integer.parseInt(info[2].trim());
        int player2Score = Integer.parseInt(info[3].trim());

        // Check for an existing inverse match
        String checkSql = "SELECT COUNT(*) FROM matches WHERE (username1 = ? AND username2 = ? AND player1_score = ? AND player2_score = ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, player2Name);
            checkStmt.setString(2, player1Name);
            checkStmt.setInt(3, player2Score);
            checkStmt.setInt(4, player1Score);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Inverse match already exists. No new record added.");
            return;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        // Insert the new match record if no inverse match exists
        String insertSql = "INSERT INTO matches (username1, username2, player1_score, player2_score) VALUES (?, ?, ?, ?)";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            insertStmt.setString(1, player1Name);
            insertStmt.setString(2, player2Name);
            insertStmt.setInt(3, player1Score);
            insertStmt.setInt(4, player2Score);

            int rowsInserted = insertStmt.executeUpdate();
            System.out.println("add matchinfo success");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
//            return false;
        }
    }
}

