//package client.GameUI;
//
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//
//public class UDPClient implements Runnable {
//    private GameUI gameUI;
//    private int port;
//    private String serverAddress;
//
//    public UDPClient(GameUI gameUI, String serverAddress, int port) {
//        this.gameUI = gameUI;
//        this.serverAddress = serverAddress;
//        this.port = port;
//    }
//
//    @Override
//    public void run() {
//        try (DatagramSocket socket = new DatagramSocket(port)) {
//            byte[] buffer = new byte[1024];
//
//            while (true) {
//                // Nhận gói tin từ server
//                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//                socket.receive(packet);
//
//                // Chuyển dữ liệu từ byte array sang String
//                String message = new String(packet.getData(), 0, packet.getLength());
//
//                // Kiểm tra xem server gửi điểm số dưới dạng "score:5"
//                if (message.startsWith("score:")) {
//                    int score = Integer.parseInt(message.split(":")[1].trim());
//                    gameUI.updateOpponentScore(score);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
