package de.htwberlin.kba.gr7;

import de.htwberlin.kba.gr7.model.GameScore;
import de.htwberlin.kba.gr7.model.PersonalGameScore;
import de.htwberlin.kba.gr7.model.User;

import java.util.List;

public interface ScoreAdministration {
    List<PersonalGameScore> getPersonalGameScores(User user);

    int getTotalWinsOfUser(User user);

    int getTotalLossesOfUser(User user);

    int insertScore(GameScore gameScore);
}
