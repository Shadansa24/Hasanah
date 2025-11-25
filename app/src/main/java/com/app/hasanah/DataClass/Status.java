package com.app.hasanah.DataClass;

public class Status {
    private int correct,mistake;

    public Status(int correct, int mistake) {
        this.correct = correct;
        this.mistake = mistake;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getMistake() {
        return mistake;
    }

    public void setMistake(int mistake) {
        this.mistake = mistake;
    }
}
