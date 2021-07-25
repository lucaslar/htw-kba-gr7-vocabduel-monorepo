package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

public interface VocabduelRoundDAO {

    boolean updateVocabduelRound(VocabduelRound round) throws GameOptimisticLockException;

    VocabduelRound selectVocabduelRoundByGameIdAndUser(User player, Long gameId) throws GameOptimisticLockException;

}
