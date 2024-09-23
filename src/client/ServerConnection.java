package client;


public class ServerConnection {
    public static void getOpponentScore(ScoreCallback callback) {
        // Mô phỏng lấy điểm số từ server
        int opponentScore = 10; // Ví dụ: điểm của đối thủ
        callback.onScoreReceived(opponentScore);
    }

    public interface ScoreCallback {
        void onScoreReceived(int score);
    }
}
