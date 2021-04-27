package de.htwberlin.kba.gr7.vocabduel.game.model;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableSet;

import java.util.List;

public class PersonalGameScore {
    private User opponent;
    private Result gameResult;
    private List<VocableSet> vocableSets;

    public User getOpponent() {
        return opponent;
    }

    public Result getGameResult() {
        return gameResult;
    }

    public List<VocableSet> getVocableSets() {
        return vocableSets;
    }
}
