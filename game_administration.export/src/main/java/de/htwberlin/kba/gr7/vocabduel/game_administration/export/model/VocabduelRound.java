package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.UntranslatedVocable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "round_finished", discriminatorType = DiscriminatorType.INTEGER)
public class VocabduelRound implements Serializable {
    @Id
    @ManyToOne(targetEntity = RunningVocabduelGame.class)
    @JoinColumn
    private RunningVocabduelGame game;
    @Id
    private int roundNr;
    @ManyToOne(optional = false)
    private UntranslatedVocable question;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<TranslationGroup> answers;
    @Enumerated(EnumType.STRING)
    private Result resultPlayerA;
    @Enumerated(EnumType.STRING)
    private Result resultPlayerB;

    public VocabduelRound() {}

    public VocabduelRound(int roundNr){ this.roundNr = roundNr; }

    public int getRoundNr() {
        return roundNr;
    }

    public UntranslatedVocable getQuestion() {
        return question;
    }

    public void setQuestion(UntranslatedVocable question) {
        this.question = question;
    }

    public List<TranslationGroup> getAnswers() {
        return answers;
    }

    public void setAnswers(List<TranslationGroup> answers) {
        this.answers = answers;
    }

    public RunningVocabduelGame getGame() {
        return game;
    }

    public void setGame(RunningVocabduelGame game) {
        this.game = game;
    }

    public Result getResultPlayerA() {
        return resultPlayerA;
    }

    public void setResultPlayerA(Result resultPlayerA) {
        this.resultPlayerA = resultPlayerA;
    }

    public Result getResultPlayerB() {
        return resultPlayerB;
    }

    public void setResultPlayerB(Result resultPlayerB) {
        this.resultPlayerB = resultPlayerB;
    }
}
