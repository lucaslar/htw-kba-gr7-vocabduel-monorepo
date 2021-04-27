package de.htwberlin.kba.gr7.vocabduel.game.model;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableSet;

import java.util.List;

public class VocabduelGame {
    private int id;
    private User playerA;
    private User playerB;
    private List<VocableSet> vocableSets;
    private List<VocabduelRound> rounds;

    public int getId() {
        return id;
    }

    public User getPlayerA() {
        return playerA;
    }

    public void setPlayerA(User playerA) {
        this.playerA = playerA;
    }

    public User getPlayerB() {
        return playerB;
    }

    public void setPlayerB(User playerB) {
        this.playerB = playerB;
    }

    public List<VocableSet> getVocableSets() {
        return vocableSets;
    }

    public void setVocableSets(List<VocableSet> vocableSets) {
        this.vocableSets = vocableSets;
    }

    public List<VocabduelRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<VocabduelRound> rounds) {
        this.rounds = rounds;
    }
}
