package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.UntranslatedVocable;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "round_finished", discriminatorType = DiscriminatorType.INTEGER)
public class VocabduelRound {
    @ManyToOne(targetEntity = VocabduelGame.class)
    private Long gameId;
    @Id
    private int roundNr;
    @OneToOne
    private UntranslatedVocable question;
    @OneToMany
    private List<TranslationGroup> answers;

    public VocabduelRound(Long gameId){ this.gameId = gameId; }

    public VocabduelRound(Long gameId, int roundNr){ this.gameId = gameId; this.roundNr = roundNr; }

    public Long getGameId() {
        return gameId;
    }

    public int getRoundNr() {
        return roundNr;
    }

    public void setRoundNr(int roundNr) {
        this.roundNr = roundNr;
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
}
