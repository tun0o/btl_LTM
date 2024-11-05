package client;
import java.io.*;
import java.net.Socket;

public class Client1 {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public Client1() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            sendMessage("username");

            String message;
            while ((message=in.readUTF()) != null){
                if (message.startsWith("INVITE_TO_PLAY")){
                    //show Dialog
                } else if (message.startsWith("DISAGREE")){
                    //show dialog
                }
            }
            System.out.println("Kết nối đến server thành công.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMessage() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client1 client = new Client1();
        new LoginScreen(client);
    }
}



