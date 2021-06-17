package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("1")
public class FinishedVocabduelGame extends VocabduelGame {
    private int totalPointsA;
    private int totalPointsB;
    private Date finishedTimestamp;

    public FinishedVocabduelGame() {
    }

    public FinishedVocabduelGame(final VocabduelGame game) {
        super(
                game.getId(),
                game.getPlayerA(),
                game.getPlayerB(),
                game.getKnownLanguage(),
                game.getLearntLanguage(),
                game.getVocableLists()
        );
    }

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
