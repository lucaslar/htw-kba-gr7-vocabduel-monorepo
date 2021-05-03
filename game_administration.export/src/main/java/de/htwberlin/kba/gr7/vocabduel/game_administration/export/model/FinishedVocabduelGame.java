package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import java.util.Date;

public class FinishedVocabduelGame extends VocabduelGame {
    private int totalPointsA;
    private int totalPointsB;
    private Date finishedTimestamp;

    public int getPointsA() {
        return totalPointsA;
    }

    public int getTotalPointsA() {
        return totalPointsA;
    }

    public void setTotalPointsA(int totalPointsA) {
        this.totalPointsA = totalPointsA;
    }

    public int getTotalPointsB() {
        return totalPointsB;
    }

    public void setTotalPointsB(int totalPointsB) {
        this.totalPointsB = totalPointsB;
    }

    public Date getFinishedTimestamp() {
        return finishedTimestamp;
    }

    public void setFinishedTimestamp(Date finishedTimestamp) {
        this.finishedTimestamp = finishedTimestamp;
    }
}
