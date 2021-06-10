package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;

import javax.persistence.*;

@Entity
public class CorrectAnswerResult {
    @Id
    @Enumerated(EnumType.STRING)
    private Result result;
    @OneToOne
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
