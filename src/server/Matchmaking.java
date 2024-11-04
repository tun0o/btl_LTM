package server;

import java.util.LinkedList;
import java.util.Queue;

public class Matchmaking {
    private Queue<SocketHandle> waitingQueue = new LinkedList<>();

    public synchronized void addToQueue(SocketHandle player){
        if (waitingQueue.isEmpty()){
            waitingQueue.add(player);
            player.sendMessage("WAITING");
        } else {
            SocketHandle opponent = waitingQueue.poll();
//            GameSession session = new GameSession(player, opponent);
//            new Thread(session).start(); //Bắt đầu vào game
        }
    }
}
