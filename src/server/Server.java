package server;
import database.PlayerDAO;
import  client.Player;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clients = new HashMap<>(); // Lưu tên người chơi và handler
    private static PlayerDAO playerDAO = new PlayerDAO(); // Tạo đối tượng PlayerDAO
    private static List<String> randomMatchQueue = new ArrayList<>(); // Queue for players looking for a random match
    private static List<String> acceptedPlayers = new ArrayList<>(); // List for players who accepted a match

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter output;
        private BufferedReader input;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);



                // Lắng nghe các yêu cầu khác từ client
                String message;
                while ((message = input.readLine()) != null) {
                    if (message.startsWith("LOGIN")) {
                        handleLogin(message);
                    } else if (message.startsWith("INVITE")) {
                        handleInvite(message);
                    } else if (message.startsWith("SCORE")) {
                        handleScoreUpdate(message);
                    }else if (message.startsWith("FINDRANDOMMATCH")) {
                        handleRandomMatch(message);
                    }else if (message.startsWith("ACCEPT")) {
                        handleAccept(message);
                    }else if (message.startsWith("SENDSCORE")) {
                        handleSendScore(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Xóa người chơi khi ngắt kết nối
                synchronized (clients) {
                    clients.remove(username);
                }
                broadcastOnlinePlayers();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void handleSendScore(String message) {
            // Parse the message to get score, sender, and receiver usernames
            String[] parts = message.split(":");
            if (parts.length != 4) {
                System.out.println("Invalid SENDSCORE message format.");
                return;
            }
            System.out.println(message);
            String score = parts[1];
            String senderUsername = parts[2];
            String receiverUsername = parts[3];

            // Find the ClientHandler for the receiver
            ClientHandler receiverClient = clients.get(receiverUsername);
            if (receiverClient != null) {
                // Send the score update message to the receiver
                receiverClient.output.println("SCORE:" + score);
            } else {
                System.out.println("Receiver " + receiverUsername + " not found or is offline.");
            }
        }

        private void handleAccept(String message) {
            // Extract the username from the message
            String accepter = message.substring(7);

            // Add the player to the accepted players list
            synchronized (acceptedPlayers) {
                acceptedPlayers.add(accepter);

                // If two players have accepted, pair them and notify
                if (acceptedPlayers.size() >= 2) {
                    String player1 = acceptedPlayers.remove(0);
                    String player2 = acceptedPlayers.remove(0);

                    // Get their handlers to send the match message
                    ClientHandler handler1 = clients.get(player1);
                    ClientHandler handler2 = clients.get(player2);

                    if (handler1 != null && handler2 != null) {
                        System.out.println(acceptedPlayers);
                        handler1.output.println("MATCH:" + player1 + ":" + player2);
                        handler2.output.println("MATCH:" + player2 + ":" + player1);
                    }
                }
            }
        }
        private void handleRandomMatch(String message) {
            // Extract the username from the message
            String[] parts = message.split(":");
            if (parts.length < 2) return; // Validate message format
            String username = parts[1];

            synchronized (randomMatchQueue) {
                // Add the player to the queue
                randomMatchQueue.add(username);

                // Check if there are at least two players in the queue
                if (randomMatchQueue.size() > 1) {
                    // Get the first two players in the queue
                    String player1 = randomMatchQueue.remove(0);
                    String player2 = randomMatchQueue.remove(0);

                    // Retrieve the ClientHandler instances for each player
                    ClientHandler handler1 = clients.get(player1);
                    ClientHandler handler2 = clients.get(player2);

                    if (handler1 != null && handler2 != null) {
                        // Send the invitation message to both players
                        handler1.output.println("INVITERANDOMMATCH:" + player2);
                        handler2.output.println("INVITERANDOMMATCH:" + player1);
                    }
                }
            }
        }

        private void handleLogin(String message) {
            try {
                String[] parts = message.split(":");
                if (parts.length != 3) {
                    output.println("LOGIN_FAILED;Invalid format");
                    return;
                }

                String username = parts[1];
                String password = parts[2];

                // Kiểm tra đăng nhập (ví dụ với cơ sở dữ liệu hoặc danh sách hợp lệ)
                if (isValidUser(username, password)) { // Giả sử có hàm isValidUser để kiểm tra
                    this.username = username;
                    synchronized (clients) {
                        clients.put(username, this);
                    }
                    output.println("LOGIN_SUCCESS");
                    System.out.println("LOGIN_SUCCESS");
                    broadcastOnlinePlayers();
                } else {
                    output.println("LOGIN_FAILED;Incorrect username or password");
                    System.out.println("LOGIN_FAILED");
                }
            } catch (Exception e) {
                output.println("LOGIN_FAILED;Error occurred");
                e.printStackTrace();
            }
        }

        // Hàm giả định để kiểm tra tính hợp lệ của người dùng
        private boolean isValidUser(String username, String password) {
            PlayerDAO playerDAO = new PlayerDAO();
            try {
                return playerDAO.login(username, password);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        private void broadcastOnlinePlayers() {
            // Gửi danh sách người chơi online cho tất cả client
            synchronized (clients) {
                StringBuilder onlineList = new StringBuilder("ONLINE:");
                for (String name : clients.keySet()) {
                    onlineList.append(name).append(",");
                }
                for (ClientHandler client : clients.values()) {
                    client.output.println(onlineList.toString());
                }
                System.out.println(onlineList.toString());
                // Lấy tất cả người chơi từ cơ sở dữ liệu
                List<Player> allPlayers;
                try {
                    allPlayers = playerDAO.getAllPlayers(); // Lấy tất cả người chơi
                } catch (SQLException e) {
                    e.printStackTrace();
                    return; // Nếu có lỗi, thoát khỏi phương thức
                }

                // Thêm thông tin người chơi vào danh sách
                StringBuilder playerInfo = new StringBuilder("ALLPLAYER:");
                for (Player player : allPlayers) {
                    playerInfo.append(player.getUsername()).append(":")
                            .append(player.getRankPoint()).append(":")
                            .append(player.getNoWin()).append(":")
                            .append(player.getScore()).append(";");
                }

                // Gửi danh sách online và thông tin người chơi
                for (ClientHandler client : clients.values()) {
                    client.output.println( playerInfo.toString());
                }
                System.out.println(playerInfo.toString());

            }

        }

        private void handleInvite(String message) {
            String[] parts = message.split(":");
            String opponent = parts[1];
            if (clients.containsKey(opponent)) {
                clients.get(opponent).output.println("INVITERANDOMMATCH:" + username);
            }
        }

        private void handleScoreUpdate(String message) {
            String[] parts = message.split(":");
            String opponent = parts[1];
            int score = Integer.parseInt(parts[2]);
            output.println("SCORE:" + opponent + ":" + score);
        }
    }
}
