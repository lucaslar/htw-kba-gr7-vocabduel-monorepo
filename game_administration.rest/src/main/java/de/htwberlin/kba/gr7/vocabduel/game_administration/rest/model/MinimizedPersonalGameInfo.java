package de.htwberlin.kba.gr7.vocabduel.game_administration.rest.model;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;

import java.util.List;
import java.util.stream.Collectors;

public class MinimizedPersonalGameInfo {
    private Long id;
    private User opponent;
    private long totalRounds;
    private long finishedRoundsSelf;
    private long finishedRoundsOpponent;
    private SupportedLanguage knownLanguage;
    private SupportedLanguage learntLanguage;
    private List<MinimizedUsedListInfo> usedVocabularyLists;

    public MinimizedPersonalGameInfo(final RunningVocabduelGame game, final User personalizeFor) {
        final boolean isSelfA = personalizeFor.getId().equals(game.getPlayerA().getId());
        final long countA = game.getRounds().stream().filter(r -> r.getResultPlayerA() != null).count();
        final long countB = game.getRounds().stream().filter(r -> r.getResultPlayerB() != null).count();

        id = game.getId();
        knownLanguage = game.getKnownLanguage();
        learntLanguage = game.getLearntLanguage();
        usedVocabularyLists = game.getVocableLists().stream().map(MinimizedUsedListInfo::new).collect(Collectors.toList());
        totalRounds = GameService.NR_OF_ROUNDS;
        if (isSelfA) {
            opponent = game.getPlayerB();
            finishedRoundsSelf = countA;
            finishedRoundsOpponent = countB;
        } else {
            opponent = game.getPlayerA();
            finishedRoundsSelf = countB;
            finishedRoundsOpponent = countA;
        }
    }

    public Long getId() {
        return id;
    }

    public User getOpponent() {
        return opponent;
    }

    public long getTotalRounds() {
        return totalRounds;
    }

    public long getFinishedRoundsSelf() {
        return finishedRoundsSelf;
    }

    public long getFinishedRoundsOpponent() {
        return finishedRoundsOpponent;
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
                ", totalRounds=" + totalRounds +
                ", finishedRoundsSelf=" + finishedRoundsSelf +
                ", finishedRoundsOpponent=" + finishedRoundsOpponent +
                ", knownLanguage=" + knownLanguage +
                ", learntLanguage=" + learntLanguage +
                ", usedVocabularyLists=" + usedVocabularyLists +
                '}';
    }
}
