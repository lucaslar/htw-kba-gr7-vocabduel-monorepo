package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;

import javax.persistence.*;

@MappedSuperclass
public abstract class VocabduelGame {
    @OneToOne(targetEntity = User.class, orphanRemoval = true)
    private User playerA;
    @OneToOne(targetEntity = User.class, orphanRemoval = true)
    private User playerB;
    @Enumerated(EnumType.STRING)
    private SupportedLanguage learntLanguage;
    @Enumerated(EnumType.STRING)
    private SupportedLanguage knownLanguage;

    public VocabduelGame() {
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

    public SupportedLanguage getKnownLanguage() {
        return knownLanguage;
    }

    public void setKnownLanguage(SupportedLanguage knownLanguage) {
        this.knownLanguage = knownLanguage;
    }

    public SupportedLanguage getLearntLanguage() {
        return learntLanguage;
    }

    public void setLearntLanguage(SupportedLanguage learntLanguage) {
        this.learntLanguage = learntLanguage;
    }

    public void setPlayerB(User playerB) {
        this.playerB = playerB;
    }
}
