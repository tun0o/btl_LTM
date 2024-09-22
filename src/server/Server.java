package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 12345;


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đã sẵn sàng.");

            while (true) {
                Socket player1Socket = serverSocket.accept();
                System.out.println("Người chơi 1 đã kết nối.");

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


