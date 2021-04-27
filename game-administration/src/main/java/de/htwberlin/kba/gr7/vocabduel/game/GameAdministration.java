package de.htwberlin.kba.gr7.vocabduel.game;

import de.htwberlin.kba.gr7.vocabduel.game.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.util.List;

public interface GameAdministration {
    VocabduelGame startGame(User playerA, User playerB);

    List<VocabduelGame> getPersonalChallengedGames(User user);

    VocabduelRound startRound(User player, VocabduelGame game);

    CorrectAnswerResult answerQuestion(User player, VocabduelGame game);
}
