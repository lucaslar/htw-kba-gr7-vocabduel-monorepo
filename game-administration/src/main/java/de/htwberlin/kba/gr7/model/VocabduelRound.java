package de.htwberlin.kba.gr7.model;

import java.util.List;

public class VocabduelRound {
    private int gameId;
    private int roundNr;
    private UntranslatedVocable question;
    private List<TranslationGroup> answers;

    public int getGameId() {
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
