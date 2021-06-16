package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "game_finished", discriminatorType = DiscriminatorType.INTEGER)
public class VocabduelGame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(targetEntity = User.class)
    private User playerA;
    @OneToOne(targetEntity = User.class)
    private User playerB;
    @Enumerated(EnumType.STRING)
    private SupportedLanguage knownLanguage;
    @Enumerated(EnumType.STRING)
    private SupportedLanguage learntLanguage;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<VocableList> vocableLists;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<VocabduelRound> rounds;

    public VocabduelGame(){ }

    public VocabduelGame(User playerA, User playerB, SupportedLanguage knownLanguage, SupportedLanguage learntLanguage, List<VocableList> vocableLists){
        this.playerA = playerA;
        this.playerB = playerB;
        this.knownLanguage = knownLanguage;
        this.learntLanguage = learntLanguage;
        this.vocableLists = vocableLists;
        this.rounds = Arrays.asList(new VocabduelRound[GameService.NR_OF_ROUNDS]);
    }

    public Long getId() {
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

    public List<VocableList> getVocableLists() {
        return vocableLists;
    }

    public void setVocableLists(List<VocableList> vocableLists) {
        this.vocableLists = vocableLists;
    }

    public List<VocabduelRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<VocabduelRound> rounds) {
        this.rounds = rounds;
    }
}
