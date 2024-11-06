import java.io.*;
import java.net.*;
import java.sql.*;

public class ServerSide {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("New client connected");
                    handleClient(clientSocket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String result = in.readLine();  // Get the match result (win, lose, or draw)
            System.out.println("Match result received: " + result);
            
            // Update the database based on the result
            updateDatabase(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateDatabase(String result) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "username", "password")) {
            Statement stmt = connection.createStatement();
            
            if ("win".equals(result)) {
                // Update player stats for win
                stmt.executeUpdate("UPDATE players SET noWin = noWin + 1, rank_point = rank_point + 5 WHERE player_name = 'Player1'");
                stmt.executeUpdate("UPDATE players SET noWin = noWin - 1, rank_point = rank_point - 3 WHERE player_name = 'Player2'");
            } else if ("lose".equals(result)) {
                // Update player stats for lose
                stmt.executeUpdate("UPDATE players SET noWin = noWin - 1, rank_point = rank_point - 3 WHERE player_name = 'Player1'");
                stmt.executeUpdate("UPDATE players SET noWin = noWin + 1, rank_point = rank_point + 5 WHERE player_name = 'Player2'");
            } else if ("draw".equals(result)) {
                // Update player stats for draw
                stmt.executeUpdate("UPDATE players SET rank_point = rank_point + 1 WHERE player_name = 'Player1'");
                stmt.executeUpdate("UPDATE players SET rank_point = rank_point + 1 WHERE player_name = 'Player2'");
            }
            
            System.out.println("Database updated based on the result: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
