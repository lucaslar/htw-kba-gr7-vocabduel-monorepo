package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class FinishedVocabduelGame extends VocabduelGame {
    @Id
    private Long id;
    @Column(nullable = false)
    private int totalPointsA;
    @Column(nullable = false)
    private int totalPointsB;
    @Column(nullable = false)
    private Date finishedTimestamp;

    public FinishedVocabduelGame() {
    }

    public FinishedVocabduelGame(final RunningVocabduelGame game) {
        id = game.getId();
        setLearntLanguage(game.getLearntLanguage());
        setKnownLanguage(game.getKnownLanguage());
        setPlayerA(game.getPlayerA());
        setPlayerB(game.getPlayerB());
    }

    public Long getId() {
        return id;
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
