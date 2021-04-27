package de.htwberlin.kba.gr7.vocabduel.game;

import de.htwberlin.kba.gr7.vocabduel.game.model.PersonalGameScore;
import de.htwberlin.kba.gr7.vocabduel.game.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.util.List;

public interface ScoreAdministration {
    List<PersonalGameScore> getPersonalGameScores(User user);

    int getTotalWinsOfUser(User user);

    int getTotalLossesOfUser(User user);

    PersonalGameScore finishGame(User player, VocabduelGame game);
}
