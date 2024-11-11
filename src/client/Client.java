package client;
import client.GameUI.GameUI;
import client.GameUI.HomeScreen;
import client.loginGUI.src.main.Main;
import  client.LeaderBoard_Rematch.MatchResult;
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
private  MatchResult matchResult;
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
                } else if (message.startsWith("1INVITE")) {
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
                } else if (message.startsWith("INVITEREMATCH")) {
                    handleRematchInvite(message.substring(14));
                }
                else if (message.startsWith("OPPONENTSUR")) {
                    handleOpponentSur(message.substring(12));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleOpponentSur(String message){
        if(gameUI!=null)        gameUI.endGame(message,true);
    }
    private void handleMatch(String message) {
        // Split the message to get currentPlayer and opponentPlayer usernames
        String[] players = message.split(":");
        if (players.length == 2) {
            String currentPlayer = players[0];
            String opponentPlayer = players[1];
            if (homeScreen != null) {
                homeScreen.setVisible(false);
            }
if(matchResult!=null) matchResult.setVisible(false);
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
        output.println("1INVITE:" + opponent);
    }
    public void sendMatchInfo(String matchInfo) {
        output.println("MATCHINFO:" + matchInfo);
    }
    public void sendSurrender(String surrenderResult) {
        output.println("SURRENDER:" + surrenderResult);
    }
    public void sendRematch(String opponent) {
        output.println("REMATCH:" + opponent);
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
    public void sendSignUp(String username, String password) {
        System.out.println("SIGNUP:" + username + ":" + password);
        output.println("SIGNUP:" + username + ":" + password);
        this.username = username;
    }

//    public void updateScore(int score) {
//        output.println("SCORE:" + ":" + score);
//    }

    private void updateOnlinePlayers(String players) {
        this.onlinePlayers = players;// Cập nhật danh sách online trên giao diện
        System.out.println("Online players: " + onlinePlayers);
        if(homeScreen!=null){
            homeScreen.loadPlayerList(onlinePlayers,allPlayers);
        }

    }
    private void updateAllPlayers(String players) {
        this.allPlayers = players;// Cập nhật danh sách online trên giao diện
        System.out.println("All players: " + allPlayers);
    }
    public String getAllPlayers() {
        return allPlayers;
    }
    private void handleRandomInvite(String inviter) {
        // Invoke the dialog on the HomeScreen
        homeScreen.showMatchInviteDialog("random",inviter);
    }
    private void handleRematchInvite(String inviter) {
        // Invoke the dialog on the HomeScreen
//        SwingUtilities.invokeLater(() -> {
            // Giả sử bạn có một tham chiếu đến đối tượng MatchResult đang hiển thị
            matchResult.showRematchConfirmation();
//        });
    }
    private void handleInvite(String inviter) {
        // Invoke the dialog on the HomeScreen
        homeScreen.showMatchInviteDialog("invite",inviter);
    }

    private void updateOpponentScore(String score) {
        System.out.println(score);
        if (gameUI != null) {
            gameUI.updateOpponentScore(score);
        }
    }

    public void handleLoginSuccess() {
        SwingUtilities.invokeLater(() -> {
            if (loginGUI != null) {
                loginGUI.setVisible(false); // Ẩn giao diện đăng nhập
            }

            new HomeScreen(this.username, onlinePlayers,allPlayers,this); // Mở giao diện HomeScreen
            homeScreen.loadPlayerList(onlinePlayers,allPlayers);
        });
    }

    public void setLoginGUI(Main loginGUI) {
        this.loginGUI = loginGUI;
    }
    // Setter to assign the HomeScreen instance
    public void setHomeScreen(HomeScreen homeScreen) {
        this.homeScreen = homeScreen;
    }
    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult ;
    }

    public Main getLoginGUI() {
        return loginGUI;
    }

    public static void main(String[] args) throws SQLException {
//        List<Player> players = new PlayerDAO().getAllPlayers();

        // Tạo giao diện đăng nhập trước khi tạo Client
        Main loginGUI = new Main();
        String serverAdress = "26.130.249.14";
//        "192.168.1.3"

        Client client = new Client(serverAdress, 12345); // Tạo client
        client.setLoginGUI(loginGUI); // Gán giao diện đăng nhập cho client

        // Hiển thị giao diện đăng nhập
        java.awt.EventQueue.invokeLater(() -> {
            client.getLoginGUI().setVisible(true); // Hiển thị giao diện
        });
    }
}
