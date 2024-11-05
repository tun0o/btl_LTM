package client;
import client.GameUI.GameUI;
import client.GameUI.HomeScreen;
import client.loginGUI.src.main.Main;
import database.PlayerDAO;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class Client {
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Main loginGUI; // Tham chiếu đến giao diện đăng nhập
private  String username;
    private  String onlinePlayers;
    private  String allPlayers;
    private HomeScreen homeScreen; // Reference to HomeScreen
    private GameUI gameUI;

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(this::listenToServer).start(); // Bắt đầu lắng nghe server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenToServer() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                if (message.startsWith("ONLINE")) {
                    updateOnlinePlayers(message.substring(7));
                } else if (message.startsWith("INVITE")) {
                    handleInvite(message.substring(7));
                } else if (message.startsWith("SCORE")) {
                    updateOpponentScore(message.substring(6));
                } else if (message.startsWith("LOGIN_SUCCESS")) {
                    handleLoginSuccess();
                }else if (message.startsWith("ALLPLAYER")) {
                    updateAllPlayers(message.substring(10));
                }
                else if (message.startsWith("ALLPLAYER1")) {
                    updateAllPlayers(message.substring(10));
                }
                else if (message.startsWith("INVITERANDOMMATCH")) {
                    handleRandomInvite(message.substring(18));
                }else if (message.startsWith("MATCH:")) {
                    handleMatch(message.substring(6));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleMatch(String message) {
        // Split the message to get currentPlayer and opponentPlayer usernames
        String[] players = message.split(":");
        if (players.length == 2) {
            String currentPlayer = players[0];
            String opponentPlayer = players[1];

            // Create and display the GameUI
//            SwingUtilities.invokeLater(() -> {
//                GameUI gameUI = new GameUI(currentPlayer, opponentPlayer, this);
//                gameUI.createAndShowUI();
//            });
            // Initialize GameUI with player names and the Client reference
            gameUI = new GameUI(currentPlayer, opponentPlayer, this);
            gameUI.createAndShowUI(); // Display the GameUI interface

        }
    }
    public void sendScore(int score,String player,String opponent) {
        System.out.println("SENDSCORE:" + String.valueOf(score)+":"+player+":"+opponent);
        output.println("SENDSCORE:" + String.valueOf(score)+":"+player+":"+opponent);
    }
    public void sendInvite(String opponent) {
        output.println("INVITE:" + opponent);
    }
    public void accept(String username) {
        output.println("ACCEPT:" + username);
    }
    public void decline(String username) {
        output.println("DECLINE:" + username);
    }
    public void findRandomMatch() {
        output.println("FINDRANDOMMATCH:" + username);
    }

    public void sendLogin(String username, String password) {
        output.println("LOGIN:" + username + ":" + password);
        this.username = username;
    }

//    public void updateScore(int score) {
//        output.println("SCORE:" + ":" + score);
//    }

    private void updateOnlinePlayers(String players) {
        this.onlinePlayers = players;// Cập nhật danh sách online trên giao diện
        System.out.println("Online players: " + onlinePlayers);
    }
    private void updateAllPlayers(String players) {
        this.allPlayers = players;// Cập nhật danh sách online trên giao diện
        System.out.println("All players: " + allPlayers);
    }
    private void handleRandomInvite(String inviter) {
        // Invoke the dialog on the HomeScreen
        homeScreen.showMatchInviteDialog("random",inviter);
    }
    private void handleInvite(String inviter) {
        // Invoke the dialog on the HomeScreen
        homeScreen.showMatchInviteDialog("random",inviter);
    }

    private void updateOpponentScore(String score) {
        System.out.println(score);
        if (gameUI != null) {
            gameUI.updateOpponentScore(score);
        }
    }

    private void handleLoginSuccess() {
        SwingUtilities.invokeLater(() -> {
            if (loginGUI != null) {
                loginGUI.setVisible(false); // Ẩn giao diện đăng nhập
            }
            new HomeScreen(this.username, onlinePlayers,allPlayers,this); // Mở giao diện HomeScreen
        });
    }

    public void setLoginGUI(Main loginGUI) {
        this.loginGUI = loginGUI;
    }
    // Setter to assign the HomeScreen instance
    public void setHomeScreen(HomeScreen homeScreen) {
        this.homeScreen = homeScreen;
    }

    public Main getLoginGUI() {
        return loginGUI;
    }

    public static void main(String[] args) throws SQLException {
        List<Player> players = new PlayerDAO().getAllPlayers();

        // Tạo giao diện đăng nhập trước khi tạo Client
        Main loginGUI = new Main();
        Client client = new Client("localhost", 12345); // Tạo client
        client.setLoginGUI(loginGUI); // Gán giao diện đăng nhập cho client

        // Hiển thị giao diện đăng nhập
        java.awt.EventQueue.invokeLater(() -> {
            client.getLoginGUI().setVisible(true); // Hiển thị giao diện
        });
    }
}
