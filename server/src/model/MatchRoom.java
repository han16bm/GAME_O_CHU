package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class MatchRoom {
    private String roomId;
    private Integer matchId; // ID được tạo từ DB cho bản ghi match đã lưu
    private int creatorId;
    private int opponentId;
    private long startTime;
    private long endTime;
    private String status; // WAITING, PLAYING, FINISHED
    private String categoryCode;
    private String categoryName;
    private List<WordInstance> words;
    private Map<Integer, PlayerState> players = new HashMap<>();

    public MatchRoom(String roomId, int creatorId) {
        this.roomId = roomId;
        this.creatorId = creatorId;
        this.status = "WAITING";
    }

    public String getRoomId() { return roomId; }
    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }
    public int getCreatorId() { return creatorId; }
    public int getOpponentId() { return opponentId; }
    public void setOpponentId(int opponentId) { this.opponentId = opponentId; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<WordInstance> getWords() { return words; }
    public void setWords(List<WordInstance> words) { this.words = words; }
    public Map<Integer, PlayerState> getPlayers() { return players; }
    public void setPlayers(Map<Integer, PlayerState> players) { this.players = players; }
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public boolean isPlayer(int userId) {return this.creatorId == userId || this.opponentId == userId;}

    public Map<String, Object> toDto() {
        Map<String, Object> dto = new HashMap<>();
        dto.put("roomId", roomId);
        dto.put("creatorId", creatorId);
        dto.put("opponentId", opponentId);
        dto.put("startTime", startTime);
        dto.put("endTime", endTime);
        dto.put("status", status);
        dto.put("categoryCode", categoryCode);
        dto.put("categoryName", categoryName);
        // Chỉ hiển thị gợi ý và độ dài cho client; không hiển thị filled/answer để ngăn đối thủ nhìn trộm
        List<Map<String, Object>> wordDtos = new ArrayList<>();
        if (words != null) {
            for (WordInstance w : words) {
                Map<String, Object> wd = new HashMap<>();
                wd.put("hint", w.getHint());
                wd.put("length", w.getFilled().length);
                // Đối với object từ gốc ta chỉ bao gồm gợi ý/độ dài. Các mẫu
                // hiển thị riêng cho từng người chơi được hiển thị riêng qua playerRevealed map.
                wordDtos.add(wd);
            }
        }
        dto.put("words", wordDtos);
        // Tạo DTO người chơi gọn gàng (không vô tình serialize personalWords)
        Map<String, Map<String, Object>> playersDto = new HashMap<>();
        for (Map.Entry<Integer, PlayerState> e : players.entrySet()) {
            Integer uid = e.getKey();
            PlayerState ps = e.getValue();
            Map<String, Object> pd = new HashMap<>();
            pd.put("score", ps.getScore());
            pd.put("correctWords", ps.getCorrectWords());
            pd.put("lastScoreAt", ps.getLastScoreAt());
            pd.put("completionTime", ps.getCompletionTime()); // Thời gian hoàn thành hết từ
            
            // Thêm thông tin người chơi nếu có
            if (ps.getUser() != null) {
                pd.put("name", ps.getUser().getName());
                pd.put("avatar", ps.getUser().getAvatar());
                pd.put("username", ps.getUser().getUsername());
            }
            
            playersDto.put(String.valueOf(uid), pd);
        }
        dto.put("players", playersDto);

        // Bao gồm các mẫu hiển thị riêng cho từng người chơi để mỗi client chỉ render các chữ cái đã điền của mình
        Map<String, List<String>> playerRevealed = new HashMap<>();
        for (Map.Entry<Integer, PlayerState> e : players.entrySet()) {
            Integer uid = e.getKey();
            PlayerState ps = e.getValue();
            List<String> reveals = new ArrayList<>();
            List<WordInstance> pWords = ps.getPersonalWords();
            if (pWords != null) {
                for (WordInstance pw : pWords) {
                    char[] f = pw.getFilled();
                    StringBuilder sb = new StringBuilder();
                    for (char c : f) sb.append(c == 0 || c == '_' ? '_' : c);
                    reveals.add(sb.toString());
                }
            } else {
                // Phương án dự phòng: hiển thị gạch dưới cho độ dài mỗi từ gốc
                if (words != null) {
                    for (WordInstance w : words) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < w.getAnswer().length(); i++) sb.append('_');
                        reveals.add(sb.toString());
                    }
                }
            }
            playerRevealed.put(String.valueOf(uid), reveals);
        }
        dto.put("playerRevealed", playerRevealed);
        return dto;
    }
}
