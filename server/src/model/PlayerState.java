package model;

import java.util.List;
import java.util.ArrayList;

public class PlayerState {
    private int score = 0;
    private int correctWords = 0;
    private long lastScoreAt = 0;
    private long completionTime = 0; // Thời gian hoàn thành hết tất cả từ (0 nếu chưa hoàn thành)
    private User user; // Thông tin người chơi
    // Bản sao riêng của từng người chơi (trạng thái revealed/fill). Đánh dấu transient để
    // không bị serialize vào DTO. MatchRoom.toDto sẽ
    // tạo pattern hiển thị riêng cho từng người chơi thay thế.
    private transient List<WordInstance> personalWords = new ArrayList<>();

    public PlayerState() {}
    
    public PlayerState(User user) {
        this.user = user;
    }

    public void addScore(int points) {
        score += points;
        lastScoreAt = System.currentTimeMillis();
    }

    public void addCorrectWord() {
            correctWords += 1;
            lastScoreAt = System.currentTimeMillis();
    }

    public int getScore() { return score; }
    public int getCorrectWords() { return correctWords; }
    public long getLastScoreAt() { return lastScoreAt; }
    public long getCompletionTime() { return completionTime; }
    public void setCompletionTime(long completionTime) { this.completionTime = completionTime; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<WordInstance> getPersonalWords() { return personalWords; }
    public void setPersonalWords(List<WordInstance> personalWords) { this.personalWords = personalWords; }
}


