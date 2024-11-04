package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class SocketHandle implements Runnable{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;
    private HashMap<String, SocketHandle> players;
    private Matchmaking matchmaking;

    public SocketHandle(Socket socket, HashMap<String, SocketHandle> players, Matchmaking matchmaking){
        this.socket = socket;
        this.players = players;
        this.matchmaking = matchmaking;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            this.username = in.readUTF();
            players.put(username, this);

            String message;
            while ((message=in.readUTF()) != null){
                if (message.startsWith("INVITE_TO_PLAY")){
                    String[] parts = message.split(";");
                    String content = parts[0].trim();
                    String recipient = parts[1].trim();
                    sendMessagePrivate(content + ";" + this.username, recipient);
                }
                else if(message.startsWith("AGREE_PLAY")){
                    String[] parts = message.split(";");
                    String content = parts[0];
                    String recipient = parts[1];
                    sendMessagePrivate(content+";"+this.username, recipient);
//                    GameSession session = new GameSession(players.get(username), players.get(recipient));
//                    new Thread(session).start(); //Băts đầu vào game
                }
                else if(message.startsWith("DISAGREE_PLAY")){
                    String[] parts = message.split(";");
                    String content = parts[0];
                    String recipient = parts[1];
                    sendMessagePrivate(content+";"+this.username, recipient);
                }
                else if (message.startsWith("RANDOM_MATCH")){
                    matchmaking.addToQueue(this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessagePrivate(String message, String recipient){
        SocketHandle recipientHandler = players.get(recipient);
        try {
            if(recipientHandler!=null){
                recipientHandler.out.writeUTF(message);
                recipientHandler.out.flush();
            }
            else {
                out.writeUTF("PLAYER_NOT_FOUND");
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
