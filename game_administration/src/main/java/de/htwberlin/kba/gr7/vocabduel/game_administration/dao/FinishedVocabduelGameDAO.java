package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.FinishedVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface FinishedVocabduelGameDAO {
    /**
     * insert FinishedVocabduelGame and remove RunningVocabduelGame
     * @param game RunningVocabduelGame
     */
    FinishedVocabduelGame insertFinishedVocabduelGame(RunningVocabduelGame game) throws GameOptimisticLockException;

    List<FinishedVocabduelGame> selectFinishedVocabduelGamesByUser(User player) throws GameOptimisticLockException;

    boolean deleteFinishedVocabduelGamesWhereUserDoesntExist() throws GameOptimisticLockException;

}
