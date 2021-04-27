package de.htwberlin.kba.gr7.vocabduel.game;

import de.htwberlin.kba.gr7.vocabduel.game.model.PersonalGameScore;
import de.htwberlin.kba.gr7.vocabduel.game.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.sql.SQLException;
import java.util.List;

public interface ScoreAdministration {
    List<PersonalGameScore> getPersonalGameScores(User user) throws SQLException;

    int getTotalWinsOfUser(User user) throws SQLException;

    int getTotalLossesOfUser(User user) throws SQLException;

    PersonalGameScore finishGame(User player, VocabduelGame game) throws SQLException;
}
