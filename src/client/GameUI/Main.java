package client.GameUI;
import client.Client;
import client.Player;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Player currentPlayer = new Player("Player1","123");
            Player opponentPlayer = new Player("Opponent","123");

            // Địa chỉ và cổng của server để lắng nghe UDP
            String serverAddress = "localhost";
            int port = 12345;
            Client client = new Client(serverAddress,port  );
            GameUI gameUI = new GameUI("123", "123",client );
            gameUI.createAndShowUI();
        });
    }
}