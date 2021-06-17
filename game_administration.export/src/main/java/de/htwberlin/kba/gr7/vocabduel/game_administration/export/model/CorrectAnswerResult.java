package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;

import java.io.Serializable;

public class CorrectAnswerResult implements Serializable {
    private Result result;
    private TranslationGroup correctAnswer;

    public CorrectAnswerResult(Result result) {
        this.result = result;
    }

    public CorrectAnswerResult(Result result, TranslationGroup correctAnswer) {
        this.result = result;
        this.correctAnswer = correctAnswer;
    }

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
