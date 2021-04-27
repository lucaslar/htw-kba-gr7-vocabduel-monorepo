package de.htwberlin.kba.gr7.vocabduel.game;

import de.htwberlin.kba.gr7.vocabduel.game.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.util.List;

public interface ScoreAdministration {
    List<PersonalFinishedGame> getPersonalGameScores(User user);

    int getTotalWinsOfUser(User user);

    int getTotalLossesOfUser(User user);

    PersonalFinishedGame finishGame(User player, VocabduelGame game);
}
