package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class RunningVocabduelGame extends VocabduelGame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToMany
    private List<VocableList> vocableLists;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "game")
    private List<VocabduelRound> rounds;

    public RunningVocabduelGame() {
    }

    public RunningVocabduelGame(User playerA, User playerB, SupportedLanguage learntLanguage, SupportedLanguage knownLanguage, List<VocableList> vocableLists) {
        setPlayerA(playerA);
        setPlayerB(playerB);
        setLearntLanguage(learntLanguage);
        setKnownLanguage(knownLanguage);
        this.vocableLists = vocableLists;
        this.rounds = Arrays.asList(new VocabduelRound[GameService.NR_OF_ROUNDS]);
    }

    public RunningVocabduelGame(Long id, User playerA, User playerB, SupportedLanguage learntLanguage, SupportedLanguage knownLanguage, List<VocableList> vocableLists) {
        this.id = id;
        setPlayerA(playerA);
        setPlayerB(playerB);
        setLearntLanguage(learntLanguage);
        setKnownLanguage(knownLanguage);
        this.vocableLists = vocableLists;
        this.rounds = Arrays.asList(new VocabduelRound[GameService.NR_OF_ROUNDS]);
    }

    public Long getId() {
        return id;
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
