package client.LeaderBoard_Rematch;

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
            String input = in.readLine(); // Expecting format: player1Name,player2Name,result
            String[] parts = input.split(",");
            if (parts.length == 3) {
                String player1Name = parts[0];
                String player2Name = parts[1];
                String result = parts[2];
                
                System.out.println("Match result received: " + player1Name + " vs " + player2Name + " - " + result);
    
                // Update the database based on the result
                updateDatabase(player1Name, player2Name, result);
            } else {
                System.out.println("Invalid input format");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void updateDatabase(String player1Name, String player2Name, String result) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "username", "password")) {
            Statement stmt = connection.createStatement();
            
            if ("win".equals(result)) {
                stmt.executeUpdate("UPDATE players SET noWin = noWin + 1, rank_point = rank_point + 5 WHERE player_name = '" + player1Name + "'");
                stmt.executeUpdate("UPDATE players SET noWin = noWin - 1, rank_point = rank_point - 3 WHERE player_name = '" + player2Name + "'");
            } else if ("lose".equals(result)) {
                stmt.executeUpdate("UPDATE players SET noWin = noWin - 1, rank_point = rank_point - 3 WHERE player_name = '" + player1Name + "'");
                stmt.executeUpdate("UPDATE players SET noWin = noWin + 1, rank_point = rank_point + 5 WHERE player_name = '" + player2Name + "'");
            } else if ("draw".equals(result)) {
                stmt.executeUpdate("UPDATE players SET rank_point = rank_point + 1 WHERE player_name = '" + player1Name + "'");
                stmt.executeUpdate("UPDATE players SET rank_point = rank_point + 1 WHERE player_name = '" + player2Name + "'");
            }
            
            System.out.println("Database updated for players: " + player1Name + " and " + player2Name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
