package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameAdministration;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public class GameAdministrationImpl implements GameAdministration {
    @Override
    public VocabduelGame startGame(User playerA, User playerB) {
        return null;
    }

    @Override
    public List<VocabduelGame> getPersonalChallengedGames(User user) {
        return null;
    }

    @Override
    public VocabduelRound startRound(User player, VocabduelGame game) {
        return null;
    }

    @Override
    public CorrectAnswerResult answerQuestion(User player, VocabduelGame game) {
        return null;
    }
}
