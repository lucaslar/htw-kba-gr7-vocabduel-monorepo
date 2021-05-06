package de.htwberlin.kba.gr7.vocabduel.game_administration.export;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

/**
 * Service for managing game scores, i.e. both registering them and accessing historic game scores.
 *
 * @author Sebastian Kehl, Lucas Larisch
 * @version 1.0, May 2021
 */
public interface ScoreAdministration {

    /**
     * Collects and returns the games finished by a given user, personalized in respect of this player's view (e.g.
     * result = Two points for the given player, one point for its opponent &rarr; return an object marked as a
     * <code>WIN</code>). The objects of this list contain easier interpretable data than just a plain score by
     * including further relevant meta data.
     *
     * @param user User whose finished games are to be returned and for whom they are to be personalized.
     * @return List of finished games including result data from the given player's point of view.
     */
    List<PersonalFinishedGame> getPersonalGameScores(User user);

    /**
     * Counts and returns the won games of a given player.
     *
     * @param user User the wins of are to be counted.
     * @return Total number of the given player's wins.
     */
    int getTotalWinsOfUser(User user);

    /**
     * Counts and returns the lost games of a given player.
     *
     * @param user User the losses of are to be counted.
     * @return Total number of the given player's losses.
     */
    int getTotalLossesOfUser(User user);

    /**
     * Finishes a game, i.e. determines its final score and stores it. Afterwards, the finished game including its
     * result personalized from the given player's point of view including easily interpretable meta data is returned
     * (e.g. result = Two points for the given player, one point for its opponent &rarr; return an object marked as a
     * <code>WIN</code>).
     *
     * @param user User the finished game incl. result is to be personalized for.
     * @param game Game the final score of is to be determined and stored.
     * @return Finished game including result and meta-data from the given player's point of view.
     */
    PersonalFinishedGame finishGame(User user, VocabduelGame game);
    // TODO: sollte finish game nicht in game administration rein?
}
