package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class CorrectAnswerResult implements Serializable {
    @Id
    @Enumerated(EnumType.STRING)
    private Result result;
    @Id
    @OneToOne
    @JoinColumn(name = "frn_translationgroup_id")
    private TranslationGroup correctAnswer;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public TranslationGroup getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(TranslationGroup correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
