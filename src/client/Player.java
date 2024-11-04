package client;
import java.awt.*;

public class Player {
    private String username;
    private String password;
    private int rank_point;
    private int noWin;
    private int score;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;

        this.score = 0;
        this.rank_point = 0;
        this.noWin = 0;
    }



    // Getter và setter cho các thuộc tính mới
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRankPoint() {
        return rank_point;
    }

    public void setRankPoint(int rank_point) {
        this.rank_point = rank_point;
    }

    public int getNoWin() {
        return noWin;
    }

    public void setNoWin(int noWin) {
        this.noWin = noWin;
    }

    // Phương thức tăng điểm xếp hạng
    public void addRankPoint(int value) {
        this.rank_point += value;
    }

    // Phương thức tăng số trận thắng
    public void incrementWin() {
        this.noWin++;
    }

    public void setScore(int score) {
        this.score= score;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int value) {
        this.score += value;
    }
}
