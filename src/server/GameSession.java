package server;
import java.io.*;
import java.net.Socket;

public class GameSession implements Runnable {
    private Socket player1Socket;
    private Socket player2Socket;
    private DataInputStream in1, in2;
    private DataOutputStream out1, out2;
    private int player1Score = 0;
    private int player2Score = 0;

    public GameSession(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;

        try {
            in1 = new DataInputStream(player1Socket.getInputStream());
            out1 = new DataOutputStream(player1Socket.getOutputStream());

            in2 = new DataInputStream(player2Socket.getInputStream());
            out2 = new DataOutputStream(player2Socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            out1.writeUTF("START");
            out2.writeUTF("START");

            // Logic của trò chơi
            for (int i = 0; i < 10; i++) {  // Ví dụ với 10 vòng chơi
                out1.writeUTF("VÒNG " + (i + 1));
                out2.writeUTF("VÒNG " + (i + 1));

                // Nhận input từ 2 người chơi
                int p1Move = in1.readInt();
                int p2Move = in2.readInt();

                // Tính điểm dựa trên phân loại rác
                if (p1Move == p2Move) {
                    player1Score++;
                    player2Score++;
                } else if (p1Move > p2Move) {
                    player1Score++;
                } else {
                    player2Score++;
                }

                out1.writeInt(player1Score);
                out2.writeInt(player2Score);
            }

            // Kết thúc trò chơi và gửi kết quả
            if (player1Score > player2Score) {
                out1.writeUTF("THẮNG");
                out2.writeUTF("THUA");
            } else if (player1Score < player2Score) {
                out1.writeUTF("THUA");
                out2.writeUTF("THẮNG");
            } else {
                out1.writeUTF("HÒA");
                out2.writeUTF("HÒA");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                player1Socket.close();
                player2Socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

