package de.htwberlin.kba.gr7.vocabduel.game;

import de.htwberlin.kba.gr7.vocabduel.game.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

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
