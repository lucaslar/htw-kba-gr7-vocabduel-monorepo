package de.htwberlin.kba.gr7;

import de.htwberlin.kba.gr7.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.model.PersonalGameScore;
import de.htwberlin.kba.gr7.model.User;
import de.htwberlin.kba.gr7.model.VocabduelGame;

import java.sql.SQLException;
import java.util.List;

public interface ScoreAdministration {
    List<PersonalGameScore> getPersonalGameScores(User user) throws SQLException, UnauthorizedException;

    int getTotalWinsOfUser(User user) throws SQLException, UnauthorizedException;

    int getTotalLossesOfUser(User user) throws SQLException, UnauthorizedException;

    PersonalGameScore finishGame(User player, VocabduelGame game) throws UnauthorizedException, SQLException;
}
