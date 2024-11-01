package server;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameSession implements Runnable {
    private Socket player1Socket;
    private Socket player2Socket;
    private DatagramSocket udpSocket;
    private InetAddress player1Address;
    private InetAddress player2Address;
    private int player1Port;
    private int player2Port;
    private int player1Score;
    private int player2Score;

    public GameSession(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
        this.player1Score = 0;
        this.player2Score = 0;

        try {
            // Khởi tạo DatagramSocket để gửi UDP
            this.udpSocket = new DatagramSocket();

            // Lấy địa chỉ và cổng UDP của người chơi (giả định là họ đã gửi từ Client)
            BufferedReader player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            BufferedReader player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            this.player1Address = player1Socket.getInetAddress();
            this.player1Port = Integer.parseInt(player1In.readLine());
            this.player2Address = player2Socket.getInetAddress();
            this.player2Port = Integer.parseInt(player2In.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Đăng nhập, trao đổi dữ liệu, v.v.
        // Sau đó bắt đầu cập nhật điểm
        startGame();
    }

    private void startGame() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Giả lập thay đổi điểm số ngẫu nhiên
                player1Score += (int) (Math.random() * 5);
                player2Score += (int) (Math.random() * 5);

                // Gửi điểm số qua UDP
                sendScores();
            }
        }, 0, 50); // Cập nhật điểm sau mỗi 5 giây
    }

    private void sendScores() {
        try {
            // Gửi điểm người chơi 1 cho người chơi 2
            String player1ScoreMessage = "score:" + player1Score;
            byte[] player1Data = player1ScoreMessage.getBytes();
            DatagramPacket player1Packet = new DatagramPacket(player1Data, player1Data.length, player2Address, player2Port);
            udpSocket.send(player1Packet);

            // Gửi điểm người chơi 2 cho người chơi 1
            String player2ScoreMessage = "score:" + player2Score;
            byte[] player2Data = player2ScoreMessage.getBytes();
            DatagramPacket player2Packet = new DatagramPacket(player2Data, player2Data.length, player1Address, player1Port);
            udpSocket.send(player2Packet);

            System.out.println("Đã gửi điểm số: Người chơi 1 - " + player1Score + ", Người chơi 2 - " + player2Score);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
