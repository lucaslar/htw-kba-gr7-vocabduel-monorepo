package de.htwberlin.kba.gr7.vocabduel.game_administration.model;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.model.TranslationGroup;

public class CorrectAnswerResult {
    private Result result;
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
