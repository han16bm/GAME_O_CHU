package model;

import java.util.List;

public class WordInstance {
    private String answer;
    private String hint;
    private char[] filled;
    private boolean[] wasTried;
    private boolean bonusGiven;

    public WordInstance(String answer, String hint) {
        this.answer = answer.toUpperCase();
        this.hint = hint;
        this.filled = new char[answer.length()];
        this.wasTried = new boolean[answer.length()];
        for (int i = 0; i < answer.length(); i++) filled[i] = '_';
    }

    public boolean handleInput(int idx, char ch) {
        char target = Character.toUpperCase(answer.charAt(idx));
        char input = Character.toUpperCase(ch);
        if (target == input) {
            filled[idx] = answer.charAt(idx); 
            return true;
        }
        wasTried[idx] = true;
        return false;
    }

    public boolean isFullyCorrect() {
        for (int i = 0; i < answer.length(); i++)
            if (filled[i] != answer.charAt(i)) return false;
        return true;
    }

    public String getAnswer() { return answer; }
    public String getHint() { return hint; }
    public char[] getFilled() { return filled; }
    public boolean[] getWasTried() { return wasTried; }
    public boolean isBonusGiven() { return bonusGiven; }

    public boolean wasTried(int idx) { return wasTried[idx]; }
    public void setTried(int idx, boolean value) { wasTried[idx] = value; }
    public void setBonusGiven(boolean bonusGiven) { this.bonusGiven = bonusGiven; }

    // // Hiển thị ngẫu nhiên một số chữ cái trong từ
    // public void revealRandomLetters(int count) {
    //     if (count <= 0) return;
    //     java.util.List<Integer> idxs = new java.util.ArrayList<>();
    //     for (int i = 0; i < answer.length(); i++) {
    //         if (filled[i] != answer.charAt(i)) idxs.add(i);
    //     }
    //     java.util.Collections.shuffle(idxs, new java.util.Random());
    //     int n = Math.min(count, idxs.size());
    //     for (int k = 0; k < n; k++) {
    //         int idx = idxs.get(k);
    //         filled[idx] = answer.charAt(idx);
    //         wasTried[idx] = true; 
    //     }
    // }

    // Hiển thị các ô hint tại các vị trí chỉ định
    public void revealAtIndices(List<Integer> indices) {
        if (indices == null || indices.isEmpty()) return;
        for (Integer idx : indices) {
            if (idx >= 0 && idx < filled.length) {
                filled[idx] = answer.charAt(idx);
                wasTried[idx] = true;
            }
        }
    }
}


