package de.htwberlin.kba.gr7.model;

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
