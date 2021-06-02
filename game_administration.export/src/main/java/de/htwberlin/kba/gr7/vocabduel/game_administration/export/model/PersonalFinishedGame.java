package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import java.util.Date;
import java.util.List;

public class PersonalFinishedGame {
    private User opponent;
    private GameResult gameResult;
    private int ownPoints;
    private int opponentPoints;
    private List<VocableList> vocableLists;
    private List<FinishedVocabduelRound> rounds;
    private Date finishedTimestamp;

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }

    public int getOwnPoints() {
        return ownPoints;
    }

    public void setOwnPoints(int ownPoints) {
        this.ownPoints = ownPoints;
    }

    public int getOpponentPoints() {
        return opponentPoints;
    }

    public void setOpponentPoints(int opponentPoints) {
        this.opponentPoints = opponentPoints;
    }

    public List<VocableList> getVocableLists() {
        return vocableLists;
    }

    public void setVocableLists(List<VocableList> vocableLists) {
        this.vocableLists = vocableLists;
    }

    public List<FinishedVocabduelRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<FinishedVocabduelRound> rounds) {
        this.rounds = rounds;
    }

    public Date getFinishedTimestamp() {
        return finishedTimestamp;
    }

    public void setFinishedTimestamp(Date finishedTimestamp) {
        this.finishedTimestamp = finishedTimestamp;
    }
}
