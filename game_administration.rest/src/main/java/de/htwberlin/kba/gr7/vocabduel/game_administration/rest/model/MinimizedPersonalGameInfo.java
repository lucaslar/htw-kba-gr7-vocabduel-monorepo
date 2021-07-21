package de.htwberlin.kba.gr7.vocabduel.game_administration.rest.model;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;

import java.util.List;
import java.util.stream.Collectors;

public class MinimizedPersonalGameInfo {
    private Long id;
    private User opponent;
    private SupportedLanguage knownLanguage;
    private SupportedLanguage learntLanguage;
    private List<MinimizedUsedListInfo> usedVocabularyLists;

    public MinimizedPersonalGameInfo(final RunningVocabduelGame game, final User personalizeFor) {
        id = game.getId();
        knownLanguage = game.getKnownLanguage();
        learntLanguage = game.getLearntLanguage();
        opponent = personalizeFor.getId().equals(game.getPlayerA().getId()) ? game.getPlayerB() : game.getPlayerA();
        usedVocabularyLists = game.getVocableLists().stream().map(MinimizedUsedListInfo::new).collect(Collectors.toList());
        // TODO Add rounds stats
    }

    public Long getId() {
        return id;
    }

    public User getOpponent() {
        return opponent;
    }

    public SupportedLanguage getKnownLanguage() {
        return knownLanguage;
    }

    public SupportedLanguage getLearntLanguage() {
        return learntLanguage;
    }

    public List<MinimizedUsedListInfo> getUsedVocabularyLists() {
        return usedVocabularyLists;
    }

    @Override
    public String toString() {
        return "MinimizedPersonalGameInfo{" +
                "id=" + id +
                ", opponent=" + opponent +
                ", knownLanguage=" + knownLanguage +
                ", learntLanguage=" + learntLanguage +
                ", usedVocabularyLists=" + usedVocabularyLists +
                '}';
    }
}
