//package client.GameUI;
//import client.Player;
//
//import javax.swing.*;
//
//public class Main1 {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            Player currentPlayer = new Player("Player1","123");
//            currentPlayer.setStatus("Playing");
//            Player opponentPlayer = new Player("Opponent","123");
//            opponentPlayer.setStatus("Playing");
//
//            // Địa chỉ và cổng của server để lắng nghe UDP
//            String serverAddress = "localhost";
//            int port = 12345;
//
//            GameUI gameUI = new GameUI(currentPlayer, opponentPlayer, serverAddress, port);
//            gameUI.createAndShowUI();
//        });
//    }
//}