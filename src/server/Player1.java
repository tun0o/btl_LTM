package server;
import java.io.*;
import java.net.Socket;

public class Player1 {
    private String username;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private double rankPoints;

    public Player1(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    public String receiveMessage() throws IOException {
        return in.readUTF();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(double rankPoints) {
        this.rankPoints = rankPoints;
    }

    public Socket getSocket() {
        return socket;
    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
