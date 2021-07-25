package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface RunningVocabduelGameDAO {

    /**
     * insert RunningVocabduelGame to database.
     * Because whole Game with VocabduelRounds will be inserted, we don't need
     *  a insert method in VocabduelRoundDAO
     * @param game RunningVocabduelGame to be inserted
     */
    void insertRunningVocabduelGame(RunningVocabduelGame game) throws GameOptimisticLockException;

    List<RunningVocabduelGame> selectRunningVocabduelGamesByUser(User player) throws GameOptimisticLockException;

    RunningVocabduelGame selectRunningVocabduelGameByGameIdAndUser(User user, Long gameId) throws GameOptimisticLockException;

    /**
     * delete RunningVocabduelGame and VocabduelRounds, so that
     *      delete method in VocabduelRoundsDAO is not needed
     * @return boolean true if everything went right
     *                 false if something bad happened
     */
    boolean deleteRunningVocabduelGameWhereUserDoesntExist() throws GameOptimisticLockException;

}
