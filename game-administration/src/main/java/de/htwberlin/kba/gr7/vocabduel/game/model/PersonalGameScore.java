package de.htwberlin.kba.gr7.vocabduel.game.model;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableList;

import java.util.List;

public class PersonalGameScore {
    private User opponent;
    private Result gameResult;
    private List<VocableList> vocableLists;

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public Result getGameResult() {
        return gameResult;
    }

    public void setGameResult(Result gameResult) {
        this.gameResult = gameResult;
    }

    public List<VocableList> getVocableLists() {
        return vocableLists;
    }

    public void setVocableLists(List<VocableList> vocableLists) {
        this.vocableLists = vocableLists;
    }
}
