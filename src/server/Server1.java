package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server1 {
    private static final int PORT = 12345;
    private static HashMap<String, SocketHandle> players = new HashMap();
//    private static HashMap<String, SocketHandle> playingPlayers = new HashMap<>();
    private static Matchmaking matchmaking = new Matchmaking();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đã sẵn sàng.");

            while (true) {
                Socket player1Socket = serverSocket.accept();
                System.out.println("Người chơi 1 đã kết nối.");

                SocketHandle socketHandle = new SocketHandle(player1Socket, players, matchmaking);
                new Thread(socketHandle).start();

                Socket player2Socket = serverSocket.accept();
                System.out.println("Người chơi 2 đã kết nối.");

                GameSession session = new GameSession(player1Socket, player2Socket);
                new Thread(session).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


