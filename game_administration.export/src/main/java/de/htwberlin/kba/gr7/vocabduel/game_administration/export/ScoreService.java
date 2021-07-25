package de.htwberlin.kba.gr7.vocabduel.game_administration.export;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.ScoreRecord;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

/**
 * Service for managing game scores, i.e. both registering them and accessing historic game scores.
 *
 * @author Sebastian Kehl, Lucas Larisch
 * @version 1.0, May 2021
 */
public interface ScoreService {

    /**
     * Collects and returns the games finished by a given user, personalized in respect of this player's view (e.g.
     * result = Two points for the given player, one point for its opponent &rarr; return an object marked as a
     * <code>WIN</code>). The objects of this list contain easier interpretable data than just a plain score by
     * including further relevant meta data.
     *
     * @param user User whose finished games are to be returned and for whom they are to be personalized.
     * @return List of finished games including result data from the given player's point of view.
     * @throws InvalidUserException         The user could not be found.
     * @throws UserOptimisticLockException  An OptimisticLock error in the user module occurred.
     * @throws GameOptimisticLockException  An OptimisticLock error in the game module occurred.
     */
    List<PersonalFinishedGame> getPersonalFinishedGames(User user) throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException;

    /**
     * @param user User the record of is to be counted.
     * @return Score record for the given player.
     * @throws InvalidUserException The user could not be found.
     * @throws UserOptimisticLockException  An OptimisticLock error in the user module occurred.
     * @throws GameOptimisticLockException  An OptimisticLock error in the game module occurred.
     */
    ScoreRecord getRecordOfUser(User user) throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException;

    /**
     * Finishes a game, i.e. determines its final score and stores it. Afterwards, the finished game including its
     * result personalized from the given player's point of view including easily interpretable meta data is returned
     * (e.g. result = Two points for the given player, one point for its opponent &rarr; return an object marked as a
     * <code>WIN</code>).
     * This Method is in ScoreAdministration cause it results in an object with scores.
     * All objects with scores have to be found here.
     *
     * @param user   User the finished game incl. result is to be personalized for.
     * @param gameId Id of the game the final score of is to be determined and stored.
     * @return Finished game including result and meta-data from the given player's point of view.
     * @throws UnfinishedGameException     The game does still have unfinished rounds, i.e. cannot be finished.
     * @throws NoAccessException           The triggering user/user the result is to be personalized for is not a player of the game instance.
     * @throws GameOptimisticLockException An OptimisticLock error in the game module occurred.
     */
    PersonalFinishedGame finishGame(User user, long gameId) throws UnfinishedGameException, NoAccessException, GameOptimisticLockException;
}
