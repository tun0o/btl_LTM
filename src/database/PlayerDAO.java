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
        String sql = "INSERT INTO player (username, password) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);

        int rowsInserted = stmt.executeUpdate();
        return rowsInserted > 0;
    }

    public List<Player> getAllPlayers(String username) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM player where username <> ? ";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            players.add(new Player(rs.getString(1), "Offline"));
        }

        return players;
    }
}

