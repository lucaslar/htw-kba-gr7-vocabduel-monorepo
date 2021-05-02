package de.htwberlin.kba.gr7.vocabduel.game_administration.model;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.model.TranslationGroup;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.model.UntranslatedVocable;

import java.util.List;

public class VocabduelRound {
    private Long gameId;
    private int roundNr;
    private UntranslatedVocable question;
    private List<TranslationGroup> answers;

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
