package de.htwberlin.kba.gr7.vocabduel.game_administration.rest.model;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.UntranslatedVocable;

import java.util.List;

public class MinimizedRoundInfo {
    int roundNr;
    UntranslatedVocable question;
    List<TranslationGroup> answers;

    public MinimizedRoundInfo(final VocabduelRound round) {
        roundNr = round.getRoundNr();
        question = round.getQuestion();
        answers = round.getAnswers();
    }

    public int getRoundNr() {
        return roundNr;
    }

    public UntranslatedVocable getQuestion() {
        return question;
    }

    public List<TranslationGroup> getAnswers() {
        return answers;
    }
}
