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
    @GeneratedValue(strategy = GenerationType.AUTO)
    // TODO: Makes sense? game should be derived
    private Long id;
//    @Id
    private int roundNr;
    @OneToOne
    private UntranslatedVocable question;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<TranslationGroup> answers;

    public VocabduelRound() {}

    public VocabduelRound(int roundNr){ this.roundNr = roundNr; }

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
