package de.htwberlin.kba.gr7.vocabduel.game_administration.export;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface ScoreAdministration {

    /**
     * get list of finished games with game data and game scores
     * @param user
     * @return
     */
    List<PersonalFinishedGame> getPersonalGameScores(User user);

    /**
     * get total wins
     * @param user
     * @return
     */
    int getTotalWinsOfUser(User user);

    /**
     * get total losses
     * @param user
     * @return
     */
    int getTotalLossesOfUser(User user);

    /**
     * finish game
     * @param player
     * @param game
     * @return
     */
    PersonalFinishedGame finishGame(User player, VocabduelGame game);
    // TODO: sollte finish game nicht in game administration rein?
}
