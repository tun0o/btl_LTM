//package client;
//import client.GameUI.HomeScreen;
//import client.loginGUI.src.main.Main;
//import database.PlayerDAO;
//
//
//import java.io.*;
//import java.net.*;
//import javax.swing.*;
//import java.awt.event.*;
//import java.sql.SQLException;
//import java.util.List;
//import client.Client;
//
//
//public class Client2 {
//    private Socket socket;
//    private PrintWriter output;
//    private BufferedReader input;
//    private Main loginGUI; // Tham chiếu đến giao diện đăng nhập
//    private  String username;
//    private  String onlinePlayers;
//    private  String allPlayers;
//    private HomeScreen homeScreen; // Reference to HomeScreen
//    public Client2(String serverAddress, int port) {
//        try {
//            socket = new Socket(serverAddress, port);
//            output = new PrintWriter(socket.getOutputStream(), true);
//            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            new Thread(this::listenToServer).start(); // Bắt đầu lắng nghe server
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void listenToServer() {
//        try {
//            String message;
//            while ((message = input.readLine()) != null) {
//                if (message.startsWith("ONLINE")) {
//                    updateOnlinePlayers(message.substring(7));
//                } else if (message.startsWith("INVITE")) {
//                    handleInvite(message.substring(7));
//                } else if (message.startsWith("SCORE")) {
//                    updateOpponentScore(message.substring(6));
//                } else if (message.startsWith("LOGIN_SUCCESS")) {
//                    handleLoginSuccess();
//                }else if (message.startsWith("ALLPLAYER")) {
//                    updateAllPlayers(message.substring(10));
//                }
//                else if (message.startsWith("ALLPLAYER1")) {
//                    updateAllPlayers(message.substring(10));
//                }
//                else if (message.startsWith("INVITERANDOMMATCH")) {
//                    handleInvite(message.substring(18));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void sendInvite(String opponent) {
//        output.println("INVITE:" + opponent);
//    }
//    public void findRandomMatch() {
//        output.println("FINDRANDOMMATCH:" + username);
//    }
//
//
//    public void sendLogin(String username, String password) {
//        output.println("LOGIN:" + username + ":" + password);
//        this.username = username;
//    }
//
//
//    public void updateScore(int score) {
//        output.println("SCORE:" + ":" + score);
//    }
//
//
//    private void updateOnlinePlayers(String players) {
//        this.onlinePlayers = players;// Cập nhật danh sách online trên giao diện
//        System.out.println("Online players: " + onlinePlayers);
//    }
//    private void updateAllPlayers(String players) {
//        this.allPlayers = players;// Cập nhật danh sách online trên giao diện
//        System.out.println("All players: " + allPlayers);
//    }
//    private void handleInvite(String inviter) {
//        // Invoke the dialog on the HomeScreen
//        homeScreen.showMatchInviteDialog("random",inviter);
//
//
//    }
//
//
//    private void updateOpponentScore(String scoreInfo) {
//        // Cập nhật điểm của đối thủ trên giao diện
//        System.out.println("Opponent score updated: " + scoreInfo);
//    }
//
//
//    private void handleLoginSuccess() {
//        SwingUtilities.invokeLater(() -> {
//            if (loginGUI != null) {
//                loginGUI.setVisible(false); // Ẩn giao diện đăng nhập
//            }
////            new HomeScreen(this.username, onlinePlayers,allPlayers,this); // Mở giao diện HomeScreen
//        });
//    }
//
//
//    public void setLoginGUI(Main loginGUI) {
//        this.loginGUI = loginGUI;
//    }
//    // Setter to assign the HomeScreen instance
//    public void setHomeScreen(HomeScreen homeScreen) {
//        this.homeScreen = homeScreen;
//    }
//
//
//    public Main getLoginGUI() {
//        return loginGUI;
//    }
//
//
//    public static void main(String[] args) throws SQLException {
//        List<Player> players = new PlayerDAO().getAllPlayers();
//
//
//        // Tạo giao diện đăng nhập trước khi tạo Client2
//        Main loginGUI = new Main();
//        Client2 client = new Client2("localhost", 12345); // Tạo client
//        client.setLoginGUI(loginGUI); // Gán giao diện đăng nhập cho client
//
//
//        // Hiển thị giao diện đăng nhập
//        java.awt.EventQueue.invokeLater(() -> {
//            client.getLoginGUI().setVisible(true); // Hiển thị giao diện
//        });
//    }
//}
