package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@MappedSuperclass
public abstract class VocabduelGame {
    @ManyToOne(targetEntity = User.class)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User playerA;
    @ManyToOne(targetEntity = User.class)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User playerB;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SupportedLanguage learntLanguage;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SupportedLanguage knownLanguage;

    @Version
    private Integer version;

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

    public Integer getVersion() {
        return version;
    }
}
